<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>${StoryMap.title}</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
<!-- <link rel="stylesheet" type="text/css" media="all" href="mystyle.css" /> -->
<!-- <link rel="stylesheet" type="text/css" media="aural" href="mystyle.css" /> -->
<!-- <link rel="stylesheet" type="text/css" media="braille" href="mystyle.css" /> -->
<!-- <link rel="stylesheet" type="text/css" media="embossed" href="mystyle.css" /> -->
<!-- <link rel="stylesheet" type="text/css" media="handheld" href="mystyle.css" /> -->
<!-- <link rel="stylesheet" type="text/css" media="print" href="mystyle.css" /> -->
<!-- <link rel="stylesheet" type="text/css" media="projection" href="mystyle.css" /> -->
<!-- <link rel="stylesheet" type="text/css" media="screen" href="mystyle.css" /> -->
<!-- <link rel="stylesheet" type="text/css" media="tty" href="mystyle.css" /> -->
<!-- <link rel="stylesheet" type="text/css" media="tv" href="mystyle.css" /> -->
<style type="text/css">
#readOverlay {
	display: block;
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
}
#readInner {
	text-align: left;
	line-height: 1.4em;
	margin: 1em auto;
	max-width: 800px;
}
#readInner a {
	color: blue;
	text-decoration: underline;
}
#readInner * {
	margin-bottom: 16px;
	border: none;
	background: none;
}
#readInner img {
	float: left;
	margin-right: 12px;
	margin-bottom: 12px;
}
#readInner h1 {
	display: block;
	width: 100%;
	border-bottom: 1px solid #333;
	font-size: 1.2em;
}
#readInner blockquote {
	margin-left: 3em;
	margin-right: 3em;
}
#readFooter {
	display: block;
	border-top: 1px solid #333;
	text-align: center;
}
div.footer-right {
	float: right;
	line-height: 1;
	text-align: right;
	font-size: .75em;
	margin-top: 18px
}


/* ---------------- USER-CONFIGURABLE STYLING --------------------- */

/* Size options */

.size-small {
	font-size: 12px;
}
.size-medium {
	font-size: 18px;
}
.size-large {
	font-size: 26px;
}
.size-x-large {
	font-size: 34px;
}
/* Style options */

.style-novel {
	font-family:"Palatino Linotype", "Book Antiqua", Palatino, serif;
	background: #F4F3DB;
	color: #222;
}
.style-ebook {
	font-family:Arial, Helvetica, sans-serif;
	background: #eee;
	color: #333;
}
.style-ebook h1 {
	font-family: "Arial Black", Gadget, sans-serif;
	font-weight: normal;
}
.style-newspaper {
	font-family:"Times New Roman", Times, serif;
	background: #FFF;
	color: #222;
}
.style-newspaper h1 {
	text-transform:capitalize;
	font-family: Georgia, "Times New Roman", Times, serif;
}
.style-terminal {
	font-family: "Lucida Console", Monaco, monospace;
	background: #1D4E2C;
	color: #C6FFC6;
}
/* Margin Options */

.margin-x-wide {
	width: 35%;
}
.margin-wide {
	width: 55%;
}
.margin-medium {
	width: 75%;
}
.margin-narrow {
	width: 95%;
}

/* ---------------- STORY MAP-SPECIFIC STYLES ------------------ */

</style>
<style type="text/css" media=print>
#readInner {
	width: 100% !important;
	font-size: 12pt;
}
</style>
</head>
<body>
<div id="readOverlay" class="size-large style-novel margin-narrow">
        <div id="readInner">

                <h1>${StoryMap.title}</h1>

                <#list StoryMap.storyCards as storyCard>
                        <#include "storycard.ftl">
                </#list>

                <div id="readFooter">The End</div>
        
        </div><!--readInner-->
</div><!--readOverlay-->

</body>
</html>
