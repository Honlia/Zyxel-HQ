$(function(){
	$("input[type='text']").not(".no").each(function(){
		$(this).placeholder();
	});
	$(".tabs").each(function(){
		$(this).tabs();
	});
	resize();
	$(window).resize(function(event) {
		resize();
	});


	$(".div_sele ul").hide();
	$(".div_sele span").click(function () {
	    $(".div_sele ul").show();
	})
	$(".div_sele ul li").click(function () {
	    $(".div_sele span").html($(this).find("label").html() + "<i class=\" glyphicon glyphicon-chevron-down\" style=\"font-size: 9px; font-weight: 100\"></i>")
	    $(this).parent().hide()
	})
	$(".ul_paihang li").hover(function () {
	    $(this).find(".a_share").show();
	}, function () {
	    $(this).find(".a_share").hide();
	})

	$(".carousel .div_item").hover(function () {

	    $(this).find(".div_text").stop().animate({ height: 255 }, 500, function () {


	        $(this).find("p").fadeIn(500)
	    });
	}, function () {
	    $(this).find(".div_text").stop().animate({ height: 40 }, 500);
	    $(this).find("p").fadeOut()
	})


	if ($(window).width() < 700) {
	    $('.carousel').owlCarousel({
	        items: 1,
	        loop: true,
	        nav: true,
	        margin: 0,
	        autoplay: false,
	        autoplayTimeout: 3000,
	        autoplayHoverPause: true
	    });

	    $('.carousel2').owlCarousel({
	        items: 2,
	        loop: true,
	        nav: true,
	        margin: 0,
	        autoplay: false,
	        autoplayTimeout: 4000,
	        autoplayHoverPause: true
	    });

	}
	else {
	    $('.carousel').owlCarousel({
	        items: 4,
	        loop: true,
	        nav: true,
	        margin: 0,
	        autoplay: true,
	        autoplayTimeout: 3000,
	        autoplayHoverPause: true
	    });
	    $('.carousel2').owlCarousel({
	        items: 8,
	        loop: true,
	        nav: true,
	        margin: 0,
	        autoplay: false,
	        autoplayTimeout: 4000,
	        autoplayHoverPause: true
	    });
	}
	$('.carousel').on('mousewheel', '.owl-stage', function (e) {
	    if (e.deltaY > 0) {
	        owl.trigger('next.owl');
	    } else {
	        owl.trigger('prev.owl');
	    }
	    e.preventDefault();
	});


	$('.carousel2').on('mousewheel', '.owl-stage', function (e) {
	    if (e.deltaY > 0) {
	        owl.trigger('next.owl');
	    } else {
	        owl.trigger('prev.owl');
	    }
	    e.preventDefault();
	});

    

	$(".page_caidan_wdcp ul li,.page_caidan_wdhy.page_party_wdhy ul li,.page_caidan_wdhy.page_yaoqing_wdhy ul li").click(function () {

	    $(this).find(".kancaipu_xz").toggleClass("in")
	    $(this).find(".party_hy_xz").toggleClass("in")
	})


	$(".div_sele ul").addClass("sele_ul")


	function stopPropagation(e) {
	    if (e.stopPropagation)
	        e.stopPropagation();
	    else
	        e.cancelBubble = true;
	}

	$(document).bind('click', function () {
	    $('.sele_ul').css('display', 'none');
	});

	$('.div_sele').bind('click', function (e) {
	    stopPropagation(e);
	});


    //全选
	$(".all_checked").click(function () {

	    $(this).parent().siblings("ul").find(".kancaipu_xz").addClass("in");
	    $(this).parent().siblings("ul").find(".party_hy_xz").addClass("in");

	})
    //反选
	$(".no_checked").click(function () {

	    $(this).parent().siblings("ul").find(".kancaipu_xz").toggleClass("in");
	    $(this).parent().siblings("ul").find(".party_hy_xz").toggleClass("in");
	})

    //删除
	
	$(".caidan_cp_del").click(function () {
	    if (confirm("是否确定删除该菜谱？"))
	    {
	        $(this).parents("li").remove();
	    }
	})

	$(".caidan_hy_del").click(function () {
	    if (confirm("是否确定删除该好友？")) {
	        $(this).parents("li").remove();
	    }
	})
	$(".caidan_cp_del").addClass("hide");
	$(".caidan_hy_del").addClass("hide");
	$("ul li").click(function () {
	    if ($(this).find(".caidan_hy_del").length>0)
	    {
	        $(this).find(".caidan_hy_del").toggleClass("hide");
	    }
	    if ($(this).find(".caidan_cp_del").length > 0) {
	        $(this).find(".caidan_cp_del").toggleClass("hide");
	    }
	})
    //菜单页全选、删除
	$(".checkall").click(function () {
	    $(this).parent().siblings("ul").find(".caidan_cp_del").removeClass("hide");
	    $(this).parent().siblings("ul").find(".caidan_hy_del").removeClass("hide");
	})
	$(".all_dele").click(function () {
	    if ($(this).parent().siblings("ul").find(".caidan_cp_del").length > 0) {
	        $(this).parent().siblings("ul").find(".caidan_cp_del").each(function () {
	            if (!$(this).hasClass("hide"))
	            {
	                $(this).addClass("dele");
	            }
	        })
	        if (confirm("是否删除选中的这" + $(this).parent().siblings("ul").find(".dele").length + "项？"))
	        {
	            $(this).parent().siblings("ul").find(".dele").parents("li").remove();
	        }

	    }
	    if ($(this).parent().siblings("ul").find(".caidan_hy_del").length > 0) {
	        $(this).parent().siblings("ul").find(".caidan_hy_del").each(function () {
	            if (!$(this).hasClass("hide")) {
	                $(this).addClass("dele");
	            }
	        })
	        if (confirm("是否删除选中的这" + $(this).parent().siblings("ul").find(".dele").length + "项？")) {
	            $(this).parent().siblings("ul").find(".dele").parents("li").remove();
	        }

	    }


	})




});

/*main*/
//

/*call*/
//
function resize(){
	var ht=$(window).height();
	$(".flht").height(ht);
}
$.fn.placeholder = function () {
    var $obj = this;
    var v = $(this).val();
    $obj.focus(function (event) {
        if ($obj.val() == v) {
            $obj.val("");
        }
    });
    $obj.blur(function (event) {
        if ($obj.val() == "") {
            $obj.val(v);
        }
    });
}
$.fn.tabs = function () {
    var $obj = this;
    var $tabs = $obj.find(".ts >.t");
    var $cnts = $obj.find(".cs >.c");

    $tabs.click(function (event) {
        var i = $tabs.index(this);
        $cnts.hide();
        $cnts.eq(i).show();

        $tabs.removeClass('on');
        $(this).addClass('on');

        return false;
    });
    $tabs.first().click();
}