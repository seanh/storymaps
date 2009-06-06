<html>
<body style="font-size:10px; font-family:Arial, Helvetica, sans-serif;">

<h1 style="text-align:center;">${StoryMap.title}</h1>

<table border="0">
<#list StoryMap.storyCards as storyCard>
    <#include "storycard.ftl">
</#list>
</table>

<h2 style="text-align:center;">The End</h2>
</body>
</html>
