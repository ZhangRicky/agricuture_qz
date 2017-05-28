package com.chenghui.agriculture.core.sitemesh;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.html.ExportTagToContentRule;
import org.sitemesh.tagprocessor.State;

public class ExtHtmlTagRuleBundle implements TagRuleBundle {
 
    @Override
    public void install(State defaultState, ContentProperty contentProperty,
            SiteMeshContext siteMeshContext) {
        defaultState.addRule("myScript", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("myScript"), false));
        defaultState.addRule("myStyle", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("myStyle"), false));
        defaultState.addRule("myCSS", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("myCSS"), false));
         
    }
     
    @Override
    public void cleanUp(State defaultState, ContentProperty contentProperty,
            SiteMeshContext siteMeshContext) {
         
    }
 
}