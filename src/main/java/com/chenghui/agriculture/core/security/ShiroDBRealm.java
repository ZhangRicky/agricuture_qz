package com.chenghui.agriculture.core.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenghui.agriculture.core.utils.Encodes;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.RoleType;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.service.system.ResourceService;
import com.chenghui.agriculture.service.system.RoleService;
import com.chenghui.agriculture.service.system.SystemOperationLogService;
import com.chenghui.agriculture.service.system.UserService;
import com.google.common.base.Objects;

public class ShiroDBRealm extends ExtrasAuthorizingRealm {

	private final static Logger logger = LoggerFactory.getLogger(ShiroDBRealm.class); 

	@Autowired
	private UserService userService;
	
	@Autowired
	private SystemOperationLogService systemOperationLogService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Override
	public boolean supports(AuthenticationToken authcToken) {
		if(authcToken instanceof UsernamePasswordToken) {
			logger.debug("UsernamePasswordToken class is "+UsernamePasswordToken.class);
			return true;
		}else {
			logger.debug("others");
			return false;
		}
	}
	
	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		logger.debug("token.getUsername() "+token.getUsername());
		Users user = userService.findUserByLoginName(token.getUsername());
		if (user == null) {
			logger.info(token.getUsername() + "登录失败,用户[ "+token.getUsername()+" ]不存在！");
			throw new UnknownAccountException();
		}
		Set<Role> userRoles = user.getRole();
		
		if ( userRoles==null || userRoles.size() == 0) {
			logger.info(token.getUsername() + "登录失败,用户[ "+token.getUsername()+" ]没有绑定角色！");
			throw new AuthenticationException("用户[ "+token.getUsername()+" ]没有绑定角色");
		}

		ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();  
		String remoteIP = getRemoteIP((HttpServletRequest)request);
		logger.info("用户[ "+token.getUsername()+" ]登录IP地址：" + remoteIP);
		byte[] salt = Encodes.decodeHex(user.getSalt());
		ShiroUser loginUser = new ShiroUser(
				user.getId(),
				user.getUserName(),
				user.getRealName(),
				user.getIsFirstLogin() == null || user.getIsFirstLogin() ? "false" : "false",
				remoteIP,
				userRoles,
				user.getArea());
		if (!loginUser.isAdmin && !user.getIsInUse()) {
			throw new DisabledAccountException();
		}
		if (!loginUser.isAdmin && (new Date()).after(user.getExpireDate())) {
			throw new ExpiredCredentialsException();
		}
		return new SimpleAuthenticationInfo(loginUser, user.getPassword(),ByteSource.Util.bytes(salt), getName());
 

	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		systemOperationLogService.saveSystemOperationLog(shiroUser, "登录", "Login", "系统登陆","","");
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if(shiroUser.isAdmin) {
			List<Role> roleList = roleService.findAll();
			for (Role role : roleList) {
				info.addRole(role.getRoleName());
			}
			List<Resources> resourceList = resourceService.findAll();
			info.addStringPermissions(transformResourceToString(resourceList));
		
		}else {
			Set<Role> roleSet = shiroUser.getRoles();
			for (Role role : roleSet) {
				// 基于Role的权限信息
				info.addRole(role.getRoleName());
			}
			logger.debug("基于Role的权限信息");
			// 基于Permission的权限信息
			info.addStringPermissions(transformRoleToString(shiroUser));
		}
		return info;
	}

	private Set<String> transformRoleToString(ShiroUser shiroUser) {
		Set<String> stringSet = new HashSet<String>();
		List<Resources> resourceSet = resourceService.findResourceSetByShiroUser(shiroUser);
		if(null!=resourceSet && !resourceSet.isEmpty()) {
			for (Resources resource : resourceSet) {
				stringSet.add(resource.getResourceCode());
				logger.debug("User[ "+shiroUser.getName()+" ]:" + resource.getResourceCode()+" ==> "+resource.getResourceName());
			}
		}
		return stringSet;
	}

	private Set<String> transformResourceToString(List<Resources> resourceList) {
		Set<String> stringSet = new HashSet<String>();

		for (Resources resource : resourceList) {
			stringSet.add(resource.getResourceCode());
			logger.debug(resource.getResourceCode());
		}

		return stringSet;
	}

	/**
	 * 
	 */
//	@PostConstruct
//	public void initCredentialsMatcher() {
//		
//		// 设定 Password 校验的 Hash 算法与迭代次数.
//		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
//		matcher.setHashIterations(UserService.HASH_INTERATIONS);
//		setCredentialsMatcher(matcher);
//
//	}

	public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
	
	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		public Long id;
		public String loginName;
		public String name;
		public String isFirstLogin;
		public String remoteIP;
		public Set<Role> roles = new HashSet<Role>();
		public Set<Role> approveRoles = new HashSet<Role>();
		public boolean isAdmin = false;//当且仅当角色集里包含admin类型的角色时为true
		public boolean isSuperAdmin = false;//当且仅当用户是admin时为true
		public boolean isAssessor = false;//当且仅当角色集合里包含审核类型的角色时为true
		public boolean isCommon = false;//当且仅当角色集合里包含普通类型的角色时为true
		public Set<Area> areas = new HashSet<Area>();
		public Set<Area> countyLevelCitys = new HashSet<Area>();
		public Set<Area> towns = new HashSet<Area>();
		public Set<Area> villages = new HashSet<Area>();
		public String token;

		public ShiroUser(Long id,String loginName, String name,String isFirstLogin,String remoteIP, Set<Role> roles, Set<Area> areas) {
			this.id = id;
			this.loginName = loginName;
			this.name = name;
			this.isFirstLogin = isFirstLogin;
			this.remoteIP = remoteIP;
			this.roles = roles;
			this.areas = areas;
			for(Role role: roles){
				if (role.getRoleType().getTypeKey() == RoleType.ROLE_LEVEL_ADMIN) {
					isAdmin = true;
				}
				if (role.getRoleType().getTypeKey() == RoleType.ROLE_LEVEL_ASSESSOR) {
					isAssessor = true;
					approveRoles.add(role);
				}
				if (role.getRoleType().getTypeKey() == RoleType.ROLE_LEVEL_COMMON) {
					isCommon = true;
				}
			}
			if ("admin".equals(loginName)) {
				isSuperAdmin = true;
			}
			for(Area area: areas){
				if (area.getAreaLevel()==3) { //清镇市
					countyLevelCitys.add(area);
				}else if (area.getAreaLevel()==2) {//乡镇
					towns.add(area);
				}else if (area.getAreaLevel()==1) {//村
					villages.add(area);
				}
			}
			
		}

		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}


		public String getLoginName() {
			return loginName;
		}

		public String getIsFirstLogin() {
			return isFirstLogin;
		}
		
		public Set<Role> getRoles() {
			return roles;
		}
		
		public Set<Role> getApproveRoles() {
			return approveRoles;
		}

		public boolean isAdmin() {
			return isAdmin;
		}

		public String getRemoteIP() {
			return remoteIP;
		}

		public Set<Area> getAreas() {
			return areas;
		}

		public Set<Area> getCountyLevelCitys() {
			return countyLevelCitys;
		}

		public Set<Area> getTowns() {
			return towns;
		}

		public Set<Area> getVillages() {
			return villages;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		/**
		 * 重载hashCode,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(loginName);
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (loginName == null) {
				if (other.loginName != null) {
					return false;
				}
			} else if (!loginName.equals(other.loginName)) {
				return false;
			}
			return true;
		}
	}
}
