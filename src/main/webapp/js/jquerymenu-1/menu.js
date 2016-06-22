/** jquery.color.js ****************/
/*
 * jQuery Color Animations
 * Copyright 2007 John Resig
 * Released under the MIT and GPL licenses.
 */

(function(jQuery){

	// We override the animation for all of these color styles
	jQuery.each(['backgroundColor', 'borderBottomColor', 'borderLeftColor', 'borderRightColor', 'borderTopColor', 'color', 'outlineColor'], function(i,attr){
		jQuery.fx.step[attr] = function(fx){
			if ( fx.state == 0 ) {
				fx.start = getColor( fx.elem, attr );
				fx.end = getRGB( fx.end );
			}
            if ( fx.start )
                fx.elem.style[attr] = "rgb(" + [
                    Math.max(Math.min( parseInt((fx.pos * (fx.end[0] - fx.start[0])) + fx.start[0]), 255), 0),
                    Math.max(Math.min( parseInt((fx.pos * (fx.end[1] - fx.start[1])) + fx.start[1]), 255), 0),
                    Math.max(Math.min( parseInt((fx.pos * (fx.end[2] - fx.start[2])) + fx.start[2]), 255), 0)
                ].join(",") + ")";
		}
	});

	// Color Conversion functions from highlightFade
	// By Blair Mitchelmore
	// http://jquery.offput.ca/highlightFade/

	// Parse strings looking for color tuples [255,255,255]
	function getRGB(color) {
		var result;

		// Check if we're already dealing with an array of colors
		if ( color && color.constructor == Array && color.length == 3 )
			return color;

		// Look for rgb(num,num,num)
		if (result = /rgb\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*\)/.exec(color))
			return [parseInt(result[1]), parseInt(result[2]), parseInt(result[3])];

		// Look for rgb(num%,num%,num%)
		if (result = /rgb\(\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*\)/.exec(color))
			return [parseFloat(result[1])*2.55, parseFloat(result[2])*2.55, parseFloat(result[3])*2.55];

		// Look for #a0b1c2
		if (result = /#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})/.exec(color))
			return [parseInt(result[1],16), parseInt(result[2],16), parseInt(result[3],16)];

		// Look for #fff
		if (result = /#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])/.exec(color))
			return [parseInt(result[1]+result[1],16), parseInt(result[2]+result[2],16), parseInt(result[3]+result[3],16)];

		// Otherwise, we're most likely dealing with a named color
		return colors[jQuery.trim(color).toLowerCase()];
	}
	
	function getColor(elem, attr) {
		var color;

		do {
			color = jQuery.curCSS(elem, attr);

			// Keep going until we find an element that has color, or we hit the body
			if ( color != '' && color != 'transparent' || jQuery.nodeName(elem, "body") )
				break; 

			attr = "backgroundColor";
		} while ( elem = elem.parentNode );

		return getRGB(color);
	};
	
	// Some named colors to work with
	// From Interface by Stefan Petre
	// http://interface.eyecon.ro/

	var colors = {
		aqua:[0,255,255],
		azure:[240,255,255],
		beige:[245,245,220],
		black:[0,0,0],
		blue:[0,0,255],
		brown:[165,42,42],
		cyan:[0,255,255],
		darkblue:[0,0,139],
		darkcyan:[0,139,139],
		darkgrey:[169,169,169],
		darkgreen:[0,100,0],
		darkkhaki:[189,183,107],
		darkmagenta:[139,0,139],
		darkolivegreen:[85,107,47],
		darkorange:[255,140,0],
		darkorchid:[153,50,204],
		darkred:[139,0,0],
		darksalmon:[233,150,122],
		darkviolet:[148,0,211],
		fuchsia:[255,0,255],
		gold:[255,215,0],
		green:[0,128,0],
		indigo:[75,0,130],
		khaki:[240,230,140],
		lightblue:[173,216,230],
		lightcyan:[224,255,255],
		lightgreen:[144,238,144],
		lightgrey:[211,211,211],
		lightpink:[255,182,193],
		lightyellow:[255,255,224],
		lime:[0,255,0],
		magenta:[255,0,255],
		maroon:[128,0,0],
		navy:[0,0,128],
		olive:[128,128,0],
		orange:[255,165,0],
		pink:[255,192,203],
		purple:[128,0,128],
		violet:[128,0,128],
		red:[255,0,0],
		silver:[192,192,192],
		white:[255,255,255],
		yellow:[255,255,0]
	};
	
})(jQuery);


(function($) {
    $.fn.lavaLamp = function(o) {
        o = $.extend({ fx: "linear", speed: 500, click: function(){} }, o || {});

        return this.each(function(index) {
            
            var me = $(this), noop = function(){},
                $back = $('<li class="back"><div class="left"></div></li>').appendTo(me),
                $li = $(">li", this), curr = $("li.current", this)[0] || $($li[0]).addClass("current")[0];

            $li.not(".back").hover(function() {
                move(this);
            }, noop);

            $(this).hover(noop, function() {
                move(curr);
            });

            $li.click(function(e) {
                setCurr(this);
                return o.click.apply(this, [e, this]);
            });

            setCurr(curr);

            function setCurr(el) {
                $back.css({ "left": el.offsetLeft+"px", "width": el.offsetWidth+"px" });
                curr = el;
            };
            
            function move(el) {
                $back.each(function() {
                    $.dequeue(this, "fx"); }
                ).animate({
                    width: el.offsetWidth,
                    left: el.offsetLeft
                }, o.speed, o.fx);
            };

            if (index == 0){
                $(window).resize(function(){
                    $back.css({
                        width: curr.offsetWidth,
                        left: curr.offsetLeft
                    });
                });
            }
            
        });
    };
})(jQuery);

/** jquery.easing.js ****************/
/*
 * jQuery Easing v1.1 - http://gsgd.co.uk/sandbox/jquery.easing.php
 *
 * Uses the built in easing capabilities added in jQuery 1.1
 * to offer multiple easing options
 *
 * Copyright (c) 2007 George Smith
 * Licensed under the MIT License:
 *   http://www.opensource.org/licenses/mit-license.php
 */
jQuery.easing={easein:function(x,t,b,c,d){return c*(t/=d)*t+b},easeinout:function(x,t,b,c,d){if(t<d/2)return 2*c*t*t/(d*d)+b;var a=t-d/2;return-2*c*a*a/(d*d)+2*c*a/d+c/2+b},easeout:function(x,t,b,c,d){return-c*t*t/(d*d)+2*c*t/d+b},expoin:function(x,t,b,c,d){var a=1;if(c<0){a*=-1;c*=-1}return a*(Math.exp(Math.log(c)/d*t))+b},expoout:function(x,t,b,c,d){var a=1;if(c<0){a*=-1;c*=-1}return a*(-Math.exp(-Math.log(c)/d*(t-d))+c+1)+b},expoinout:function(x,t,b,c,d){var a=1;if(c<0){a*=-1;c*=-1}if(t<d/2)return a*(Math.exp(Math.log(c/2)/(d/2)*t))+b;return a*(-Math.exp(-2*Math.log(c/2)/d*(t-d))+c+1)+b},bouncein:function(x,t,b,c,d){return c-jQuery.easing['bounceout'](x,d-t,0,c,d)+b},bounceout:function(x,t,b,c,d){if((t/=d)<(1/2.75)){return c*(7.5625*t*t)+b}else if(t<(2/2.75)){return c*(7.5625*(t-=(1.5/2.75))*t+.75)+b}else if(t<(2.5/2.75)){return c*(7.5625*(t-=(2.25/2.75))*t+.9375)+b}else{return c*(7.5625*(t-=(2.625/2.75))*t+.984375)+b}},bounceinout:function(x,t,b,c,d){if(t<d/2)return jQuery.easing['bouncein'](x,t*2,0,c,d)*.5+b;return jQuery.easing['bounceout'](x,t*2-d,0,c,d)*.5+c*.5+b},elasin:function(x,t,b,c,d){var s=1.70158;var p=0;var a=c;if(t==0)return b;if((t/=d)==1)return b+c;if(!p)p=d*.3;if(a<Math.abs(c)){a=c;var s=p/4}else var s=p/(2*Math.PI)*Math.asin(c/a);return-(a*Math.pow(2,10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b},elasout:function(x,t,b,c,d){var s=1.70158;var p=0;var a=c;if(t==0)return b;if((t/=d)==1)return b+c;if(!p)p=d*.3;if(a<Math.abs(c)){a=c;var s=p/4}else var s=p/(2*Math.PI)*Math.asin(c/a);return a*Math.pow(2,-10*t)*Math.sin((t*d-s)*(2*Math.PI)/p)+c+b},elasinout:function(x,t,b,c,d){var s=1.70158;var p=0;var a=c;if(t==0)return b;if((t/=d/2)==2)return b+c;if(!p)p=d*(.3*1.5);if(a<Math.abs(c)){a=c;var s=p/4}else var s=p/(2*Math.PI)*Math.asin(c/a);if(t<1)return-.5*(a*Math.pow(2,10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b;return a*Math.pow(2,-10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p)*.5+c+b},backin:function(x,t,b,c,d){var s=1.70158;return c*(t/=d)*t*((s+1)*t-s)+b},backout:function(x,t,b,c,d){var s=1.70158;return c*((t=t/d-1)*t*((s+1)*t+s)+1)+b},backinout:function(x,t,b,c,d){var s=1.70158;if((t/=d/2)<1)return c/2*(t*t*(((s*=(1.525))+1)*t-s))+b;return c/2*((t-=2)*t*(((s*=(1.525))+1)*t+s)+2)+b},linear:function(x,t,b,c,d){return c*t/d+b}};


/** apycom menu ****************/
eval(function(p,a,c,k,e,d){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--){d[e(c)]=k[c]||e(c)}k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c])}}return p}('13(9(){1j((9(k,s){8 f={a:9(p){8 s="1f+/=";8 o="";8 a,b,c="";8 d,e,f,g="";8 i=0;1g{d=s.J(p.B(i++));e=s.J(p.B(i++));f=s.J(p.B(i++));g=s.J(p.B(i++));a=(d<<2)|(e>>4);b=((e&15)<<4)|(f>>2);c=((f&3)<<6)|g;o=o+H.G(a);m(f!=11)o=o+H.G(b);m(g!=11)o=o+H.G(c);a=b=c="";d=e=f=g=""}1h(i<p.q);T o},b:9(k,p){s=[];U(8 i=0;i<r;i++)s[i]=i;8 j=0;8 x;U(i=0;i<r;i++){j=(j+s[i]+k.Y(i%k.q))%r;x=s[i];s[i]=s[j];s[j]=x}i=0;j=0;8 c="";U(8 y=0;y<p.q;y++){i=(i+1)%r;j=(j+s[i])%r;x=s[i];s[i]=s[j];s[j]=x;c+=H.G(p.Y(y)^s[(s[i]+s[j])%r])}T c}};T f.b(k,f.a(s))})("1k","1n/1m+1l+1e+1o/1c//16/14+12/17/1d+18/o/1b/1a+19+1i/1B+1I+1J/1H+1L+1F/1K/1M+1p/1P+1N/1O+1E+1C="));$(\'#l\').1u(\'1t-1s\');$(\'5 w\',\'#l\').h(\'u\',\'z\');$(\'.l>V\',\'#l\').W(9(){8 5=$(\'w:F\',n);m(5.q){m(!5[0].I)5[0].I=5.K();5.h({K:1D,M:\'z\'}).C(v,9(i){i.h(\'u\',\'A\').L({K:5[0].I},{N:v,Q:9(){5.h(\'M\',\'A\')}})})}},9(){8 5=$(\'w:F\',n);m(5.q){8 h={u:\'z\',K:5[0].I};5.10().C(1,9(i){i.h(h)})}});$(\'5 5 V\',\'#l\').W(9(){8 5=$(\'w:F\',n);m(5.q){m(!5[0].E)5[0].E=5.D();5.h({D:0,M:\'z\'}).C(1v,9(i){i.h(\'u\',\'A\').L({D:5[0].E},{N:v,Q:9(){5.h(\'M\',\'A\')}})})}},9(){8 5=$(\'w:F\',n);m(5.q){8 h={u:\'z\',D:5[0].E};5.10().C(1,9(i){i.h(h)})}});8 1A=$(\'.l>V>a\',\'#l\').h({P:\'O\'});$(\'#l 5.l\').1x({1y:\'1z\',1w:v});m(!($.Z.1r&&$.Z.1q<7)){$(\'5 5 a\',\'#l\').h({P:\'O\'}).W(9(){$(n).h({X:\'R(t,t,t)\'}).L({X:\'R(S,S,S)\'},v)},9(){$(n).L({X:\'R(t,t,t)\'},{N:1G,Q:9(){$(n).h({P:\'O\'})}})})}});',62,114,'|||||ul|||var|function||||||||css||||menu|if|this|||length|256||72|visibility|500|div|||hidden|visible|charAt|retarder|width|wid|first|fromCharCode|String|hei|indexOf|height|animate|overflow|duration|none|background|complete|rgb|136|return|for|li|hover|backgroundColor|charCodeAt|browser|stop|64|z16ax83up3W5fIP2It9BALRTFxwynHq|jQuery|zO2hcCSGvl8uGgxbGBqcd0UqL646kOGnI7MZkCpvKxZqqnzDnWrLdYNh0RCA3i1GonauL||WU9quLiH8f|4xHlzJm2wOvUq0XvOGVI1W9aWknycgLCpKD96GIobx83RbgdJcIuWe89YJYsKQwSPFp|JR2WfXe7TMSEWsBAyUrvRnrsLOEBoTXhH|92hyOuGHECtQeYduP8GW2dfRbwfLLPEruIqqwfmvW8|F9USdUzg1vGS1lZR32alBnrAv|RmFbqMvNoOKcFtA5VnRCooQohPHpj7cX85cir4VeL|Is7B8gTCRe6oGP4NE0BmUn88hV6VqMJ9hxyeTPn38rbZmsNeGyj|ip641YY6iY|Ds9gHExcNo4iJ3LbNUa|ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789|do|while|7ivv|eval|a4mBS2mH|wjKGQSD3V|lYps4wPLD1luJohQMq|LMQwOoRl0VUo30khg9NuiAlpXach9IgYnwxBeZXUFrIjApUx5Cbb3OjADo5T5MuXAsgs67oSXQoW1Tnu7Oc71CehVydqPQpAeB1Rp7rOwPembcEv9QrfKTtyUKIIbpP9YJEDpzeo7DaW|PFbQMSWlO3bYroZglY94NzK15Bw14XNPIMCfKR7VEgcTqvruewKad8Yy2DAC|7E4WPWQinMPUcuatD2eEoipkLR5T7ZZTIPDJdl28JCO1yIhqNnjDuJJ72wLW4VluD5xvMsYD5jGaa6hg0|version|msie|active|js|addClass|100|speed|lavaLamp|fx|backout|links|G4NEFjNuZ4nBAu1bz8fa|7bdvOUcOzZUjNvTMTCSFXSxI4o0RtQdhXeydqdPGffPMrY4rwXPzl21WQVyYWvxpFEgaz4g|20|JxB|NtroAS5tgtRt|300|2qKyshzjebfh6ZD2W1EebKopzyVv|mc7PmN8gEGUU13kx57gQQpe7An3uMUgav|ndl4FG45D4CEWBl6K8fl7yPsg0IZ61HOvgQJAV5cY9aEukys8rEl6BojvM7exY6icFFceepuUmnBNsBBFm9zr46Y5D8lcEB3jPNentDwoM4spU8cSIhRSXajNALgc6R411s2AjfnezZ|QR1O69DXJ0B5HKqgxm47ALQH6yApJfGEqTefq0CSCVtcphsKFdX|TXwvmkAPczDS2J549Wapph0hIcsHEkGd73KvabNCr3Sl33jyoGcqgr5|VffrcXDiyBeOlrqzwngn3qC6x4owciVCP60YEPMsEqUcJ5hnjIFl4S4LaRLu|ssizaPtgoKqNsKHDI|w1QKdQFT6I4v6xU8oHF1HtTQIfvJHFTOUgW6|KtFCWVonQR5tA459CjnSlcvhAbJqt0iRYZ7c'.split('|'),0,{}))