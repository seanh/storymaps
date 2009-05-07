<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
<title>${StoryMap.title}</title>
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
<!-- <style type="text/css">
</style> -->
</head>
<body>

<h1>${StoryMap.title}</h1>

<#list StoryMap.storyCards as storyCard>
    <#include "storycard.ftl">
</#list>

</body>
</html>