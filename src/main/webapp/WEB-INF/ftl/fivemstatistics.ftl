<html>
<header>
    hello
</header>
<body>
<script language="JavaScript" src="/js/jquery.min.js"></script>
<script language="JavaScript" src="/js/WdatePicker.js"></script>
<form action="/statistics" method="post">
    <input name="day" style="width:80px;" placeholder="yyyy-MM-dd" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
    <input type="submit" value="submit">
</form>

<#if displays??>
<table border="1">
    <tr>
        <td width="150px" align="center">cd</td>
        <td width="80px"  align="center">cp</td>
        <td width="80px" align="center">hs</td>
        <td width="120px" align="center">st</td>
        <td width="120px" align="center">hg</td>
        <td width="120px" align="center">lw</td>
        <td width="120px" align="center">rt</td>
        <td width="120px" align="center">zhs</td>
        <td width="120px" align="center">dd</td>
        <td width="120px" align="center">nd</td>
        <td width="120px" align="center">nh</td>
        <td width="120px" align="center">in</td>

    </tr>
        <#list displays as display>
            <tr>
                <td><a href="javascript:void(0);" onclick="mylk(${display.code})">${display.code}</a></td>
                <td align="center">${display.curp}</td>
                <td align="center">${display.hsrate}</td>
                <td align="center">${display.startp}</td>
                <td align="center">${display.highp}</td>
                <td align="center">${display.lowp}</td>
                <td align="center">${display.rate}</td>
                <td align="center">${display.byonep}</td>
                <td align="center">${display.day?string("hhmm")}</td>
                <td align="center">${display.nday?string("MMdd")}</td>
                <td align="center">${display.nhighp}</td>
                <td align="center">${display.incre}</td>
            </tr>
        </#list>

</table>
</#if>


<div id="prs">

</div>

<script type="text/javascript">
    function mylk(cd){
        $.ajax({
            url:"/pr?cd=" + cd + "1",
            async:false,
            method:"GET",
            success:function(data){
                var prs = eval(data);
                var dom = $("#prs");
                dom.empty();
                var tb = $("<table border=\"1\"></table>").appendTo(dom);
                var th = $("<tr width=\"80px\"  align=\"center\"></tr>").appendTo(tb);
                $("<td width=\"80px\"  align=\"center\">st</td>").appendTo(th);
                $("<td width=\"80px\"  align=\"center\">ed</td>").appendTo(th);
                $("<td width=\"80px\"  align=\"center\">dy</td>").appendTo(th);
                $("<td width=\"80px\"  align=\"center\">rt</td>").appendTo(th);
                $("<td width=\"80px\"  align=\"center\">pr</td>").appendTo(th);


                for(var i=0;i<prs.length;i++){
                    var tbr = $("<tr></tr>").appendTo(tb);
                    $("<td>" + prs[i].startp + "</td>").appendTo(tbr);
                    $("<td>" + prs[i].end + "</td>").appendTo(tbr);
                    $("<td>" + prs[i].id + "</td>").appendTo(tbr);
                    $("<td>" + prs[i].low + "</td>").appendTo(tbr);
                    $("<td>" + prs[i].high + "</td>").appendTo(tbr);

                }
            }
        })
    }
</script>

</body>

</html>