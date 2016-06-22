<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<script type="text/javascript">
    $(document).ready(function () {
        //loadFeed();
        loadWeatherFeed();
    });
    var isNewsFeedHidden = true;
    var iRSS2Counter = 1;
    var rss1URLS = new Array();
    var rss2URLS = new Array();

    rss1URLS[0] = 'http://www.ibm.com/developerworks/views/java/rss/libraryview.jsp';
    rss1URLS[1] = 'http://www.springsource.org/node/feed';
    rss1URLS[2] = 'http://www.artima.com/buzz/feeds/buzz.rss';
    rss1URLS[3] = 'http://feeds.feedburner.com/myandroidmag';
    rss1URLS[4] = 'http://feeds.feedburner.com/NDTV-Tech';
    rss1URLS[5] = 'http://www.geek.com/articles/android/feed/';

    rss2URLS[0] = 'http://msdn.microsoft.com/en-us/magazine/rss/default.aspx?z=z&all=1';
    rss2URLS[1] = 'http://www.netmag.co.uk/zine/develop.rss';
    rss2URLS[2] = 'http://www.c-sharpcorner.com/Rss/LatestArticles.aspx';
    rss2URLS[3] = 'http://www.code-magazine.com/CodeRSS.aspx';
    rss2URLS[4] = 'http://opensource.sys-con.com/index.rss';
    rss2URLS[5] = 'http://www.mactech.com/news/feed';


    function loadFeed() {
        if (isNewsFeedHidden)
            return;

        if (iRSS2Counter == 7)
            iRSS2Counter = 1;
        $('#id_RSS1').feedget({
            feed:rss1URLS[iRSS2Counter - 1],
            images:'true',
            loadingImg:'<s:url value="/images/loading.gif"/>'
        });
        //$('#id_RSS2').width=$('#id_BodyText').width;
        $('#id_RSS2').feedget({
            feed:rss2URLS[iRSS2Counter - 1],
            direction:'horizontal',
            images:'true',
            loadingImg:'<s:url value="/images/loading.gif"/>'
        });

        iRSS2Counter = iRSS2Counter + 1;
    }
    function loadWeatherFeed() {
        $('#id_KolWeather').feedget({
            feed:'http://weather.yahooapis.com/forecastrss?w=12586798&u=c',
            direction:'horizontal',
            buttons:'false',
            images:'true',
            loadingImg:'<s:url value="/images/loading.gif"/>'
        });
        $('#id_Shawnee').feedget({
            feed:'http://weather.yahooapis.com/forecastrss?w=2438265&u=c',
            direction:'horizontal',
            buttons:'false',
            images:'true',
            loadingImg:'<s:url value="/images/loading.gif"/>'
        });
    }
    var timerId1 = setInterval(loadFeed, 60000);
    var timerId2 = setInterval(loadWeatherFeed, 60000);

    function showHide(showOrHide) {
        if (showOrHide == 'show') {
            $("#id_NewsDiv").show('slow');
            $("#id_Hide").show();
            $("#id_Show").hide();
            isNewsFeedHidden = false;
            loadFeed();
        }
        else {
            $("#id_NewsDiv").hide('slow');
            $("#id_Hide").hide();
            $("#id_Show").show();
            isNewsFeedHidden = true;
        }
    }
</script>
<table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="100%" class="box_border" colspan="3">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">Welcome To Enterprise Software Kolkata Home
                        Page
                    </th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                    <%-- <th colspan="60%" align="right" valign="top" class="table_top"><a style="padding-right: 20px" href="<s:url action="loginView"/> ">Login</a></th> --%>
                </tr>

                <tr>
                    <td class="body_text">
                        <embed src="<s:url value="/images/PerKolIntro.swf"/>" quality="high"
                               type="application/x-shockwave-flash"
                               wmode="transparent" width="480" height="250"
                               pluginspage="http://www.macromedia.com/go/getflashplayer"
                               allowScriptAccess="always"></embed>
                    </td>
                    <td class="body_text" id="id_BodyText">

                        In 2010, Enterprise Software (then Perceptive Software) joined Lexmark International as a stand-alone
                        business unit, and set up its first offshore development unit in Kolkata.
                        With Enterprise Software technology, including ImageNow document management, document
                        imaging
                        and workflow, the user can capture, process and collaborate on important documents and
                        content,
                        protect data integrity throughout its lifecycle and access precise content in the context of
                        everyday business processes. With a team of around 80-odd resources, spread across
                        'Platform'
                        and 'Solutions', the Enterprise Kolkata team is dedicated towards integrating these
                        enterprise
                        content management (ECM) capabilities with various software platforms, operating systems or
                        applications.<br/>
                        <b>Solutions</b><br/>
                        Enterprise Software Solutions are fueled by our ECM technology, including the ImageNow
                        document
                        management, document imaging and workflow suite. Integrating ImageNow capabilities with
                        existing
                        applications like Microsoft SharePoint, Dynamics AX or Dynamics CRM, the Solution teams in
                        Kolkata deliver "specialized solutions" in matter of weeks or months. Success of the IHE
                        team at
                        the Connectathon and the HIMSS Interoperability showcase in 2012 led to new ideas about
                        building
                        healthcare solutions around ImageNow.<br/>
                        <b>Platform</b><br/>
                        The platform teams in Kolkata mainly comprise the Mobile and Web Client teams. The Mobile
                        teams
                        are engaged in integrating ImageNow with Android operating system, integrating ImageNow
                        forms,
                        and reproducing the ImageNow UI on Android-based phones and tablets. The Web Client teams
                        are
                        committed to developing browser-based solutions for the virtual workforce to access and
                        maintain
                        ImageNow data on desktop as well as tablet devices such as the Apple iPad and Android
                        tablets.

                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <ul>
                            <li>
                                <img id="id_Show" src="<s:url value="/images/list-add.png"/>" style="cursor: pointer"
                                     onclick="showHide('show')" /><img id="id_Hide" style="display: none;cursor: pointer"
                                                                      src="<s:url value="/images/list-remove.png"/>"
                                                                      onclick="showHide('hide')"/><b><i>News
                                Update From Tech. World</i></b>
                            </li>
                        </ul>
                    </td>
                </tr>
                <tr id="id_NewsDiv" style="display: none;">
                    <td>
                        <div id="id_RSS1" style="height:250px; margin:auto;"></div>
                    </td>
                    <td>
                        <div id="id_RSS2" style="width:1150px;height:250px; margin:auto;"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <table width="100%">
                            <tr>
                                <td colspan="25%" align="center">
                                    <div id="id_KolWeather" style="height:250px; margin:auto;"></div>
                                </td>
                                <td colspan="25%" align="center">
                                    <div id="id_Shawnee" style="height:250px; margin:auto;"></div>
                                </td>
                                <td colspan="25%" align="center">
                                    <div style="height:250px; margin:auto;">
                                        <table border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td align="center">
                                                    <script type="text/javascript"
                                                            src="http://www.worldtimeserver.com/clocks/embed.js"></script>
                                                    <script type="text/javascript"
                                                            language="JavaScript">objUSKS = new Object;
                                                    objUSKS.wtsclock = "wtsclock001.swf";
                                                    objUSKS.color = "6495ED";
                                                    objUSKS.wtsid = "US-KS";
                                                    objUSKS.width = 200;
                                                    objUSKS.height = 200;
                                                    objUSKS.wmode = "transparent";
                                                    showClock(objUSKS);</script>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="center"><b>Lenexa</b></td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                                <td colspan="25%" align="center">
                                    <div style="height:250px; margin:auto;">
                                        <table border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td align="center">
                                                    <script type="text/javascript"
                                                            src="http://www.worldtimeserver.com/clocks/embed.js"></script>
                                                    <script type="text/javascript"
                                                            language="JavaScript">objIN = new Object;
                                                    objIN.wtsclock = "wtsclock001.swf";
                                                    objIN.color = "6495ED";
                                                    objIN.wtsid = "IN";
                                                    objIN.width = 200;
                                                    objIN.height = 200;
                                                    objIN.wmode = "transparent";
                                                    showClock(objIN);</script>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="center"><b>Kolkata</b></td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>