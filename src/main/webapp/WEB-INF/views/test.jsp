<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="Accept-CH" content="DPR, Width, Viewport-Width">
<style>
.demo-area{
  background:$color_invert_fg;
  border-radius:8px;
  padding:20px;
  section{
    padding-top:0;
  }
}

.demo-trigger {
  display: inline-block;
  width: 30%;
  float: left;
}

.detail {
  position: relative;
  width: 65%;
  margin-left: 5%;
  float: left;
  button{
    vertical-align:middle;
    opacity:.5;
    cursor:unset;
    background:$color_invert_chrome_tint;
    margin-left:1em;
  }
}


@media (max-width: 610px) {

  .detail, .demo-trigger {
    float: none;
  }

  .demo-trigger {
    display:block;
    width: 50%;
    max-width:200px;
    margin: 0 auto;
  }

  .detail {
    margin: 0;
    width: auto;
  }

  p {
    margin: 0 auto 1em;
  }

  .responsive-hint {
    display: none;
  }
  h3 {
    margin-top:20px;
  }
}
</style>
<link rel="stylesheet" href="resources/plugins/drift/drift-basic.css"/>
<script src="resources/plugins/drift/Drift.min.js"></script>
</head>
<body>
<section class="content">
  <article class="demo-area">
  <img class="demo-trigger" src="https://demos.imgix.net/wristwatch.jpg?w=200&ch=DPR&dpr=2&border=1,ddd" data-zoom="https://demos.imgix.net/wristwatch.jpg?w=400&h=400&ch=DPR&dpr=2">
  <div class="detail">
   <section>
      <h3>Men's Watch - Drift Demo</h3>
      <p>Specifications:</p>
      <ul>
        <li>Hover over image</li>
        <li>35 mm stainless steel case</li>
        <li>Stainless link bracelet with buckle closure</li>
        <li>Water resistant to 100m</li>
      </ul>
      <h4>$XX.XX <button>Add to Cart</button></h4>
    </section>
  </div>
  </article>
</section>
<section class="content">
  <article>
     <h1>Drift</h1>
    <p>This is a demo of Drift, a simple, lightweight, no-dependencies JavaScript "zoom on hover" tool from <a href="http://imgix.com">imgix</a>. Move your mouse over the image (or touch it) to see it in action. This demo uses the simple included theme, but it's very easy to extend and customize to fit your needs. You can <a href="https://github.com/imgix/drift">learn more and download it here</a>.</p>
          <p class="responsive-hint">(Psst…&nbsp;try making your browser window smaller!)</p>
     <div class="button-group">
       <a href="https://github.com/imgix/luminous" class="button" target="_blank">View on github</a>
       <a href="https://imgix.com" target="_blank" class="button">Visit imgix.com</a>
    </div>
  </article>
</section>
<footer>
  <hr>
<a href="https://imgix.com"><img src="https://assets.imgix.net/presskit/imgix-presskit.pdf?page=3&fm=png&w=320&dpr=2" width="160" height="60"></a>
</footer>
<script type="text/javascript">
var demoTrigger = document.querySelector('.demo-trigger');
var paneContainer = document.querySelector('.detail');

new Drift(demoTrigger, {
  paneContainer: paneContainer,
  inlinePane: false
});
</script>
<body>