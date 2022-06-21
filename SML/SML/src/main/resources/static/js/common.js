function fnOpenPopup() {
    var args = arguments;
    var name = args[0];
    var url = args[1];
    var width = args[2];
    var height = args[3];
    var scrollbars = args[4]!=null?args[4]:'no';

    // var leftpos = (document.body.offsetWidth / 2) - (width / 2);   	// 싱글모니터에서 팝업창 중앙정렬
    // var leftpos = (document.body.offsetWidth / 2) - (width / 2) + window.screenLeft;		// 듀얼모니터에서 화면정렬
    var leftpos = (document.body.offsetWidth / 2) - (width / 2) + window.screenLeft + 100;	// 듀얼모니터 정렬 + 좌측에서 100(px) 떨어진만큼
    var toppos = (window.screen.height / 2) - (height / 2);

    if (!url || url == "") url = "about:blank";
    return window.open(url, name, 'scrollbars='+scrollbars+', toolbar=no, location=no, status=no, menubar=no, resizable=no, width='+width+', height='+height+', left='+leftpos+', top='+toppos);
}


// 마스크 생성
function wrapWindowByMask() {
    var args = arguments; //Parameters
    var windowByMask = args[0] == null ? '#windowByMask' : args[0];

    //화면의 높이와 너비를 구한다.
    var maskHeight = $(document).height();
    var maskWidth = $(window).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $(windowByMask).css({'width':maskWidth, 'height':maskHeight});

    //애니메이션 효과 - 일단 1초동안 까맣게 됐다가 80% 불투명도로 간다.
    $(windowByMask).show();
    //$(windowByMask).fadeIn(1);
    //$(windowByMask).fadeTo("slow", 0.9);
}

// 마스크 제거
function wrapWindowByUnMask() {
    var args = arguments; //Parameters
    var windowByMask = args[0] == null ? '#windowByMask' : args[0];

    $(windowByMask).hide();
}

function fnAjaxSubmit() {
    var args = arguments; //Parameters
    var url = args[0];
    var frm = args[1];
    var callbackFunction = args[2];
    var msg = args[3];
    var mask = args[4]!=null?args[4]:false;
    var errorCallbackFunction = args[5]!=null?args[5]:false;

    if (mask && $("#windowByMask").size()==0) {
        $("body").append('<div id="windowByMask"><img src="/res/img/loading1.gif" /></div>');
    }

    //if (AutoValidate($(freefrm).find('input, select, textarea')) == false) return;

    //fnFormatUnmask($(frm));

    $(frm).ajaxSubmit({
        dataType: 'json'
        , url: url
        , success: function(data){
            if(mask) wrapWindowByUnMask();
            if (msg == null) {
                if (data != null && data.message != null && data.message != undefined) {
                    alert(data.message.trim()); //성공 메시지 처리
                }
            } else if (msg != "") {
                // 자바에서 올라오는 BusinessException을 먼저 체크한 후 사용자메세지를 체크한다.
                if (data != null && data.message != null && data.message != undefined) {
                    alert(data.message.trim());
                } else{
                    alert(msg.trim()); //사용자 메시지 처리
                    if (errorCallbackFunction != null && errorCallbackFunction != undefined && errorCallbackFunction != false) {
                        errorCallbackFunction(data); // User Function Call
                    }
                }
            }
            if (callbackFunction != null && callbackFunction != undefined) {
                callbackFunction(data); //User Function Call
            }
        }
        , error: function(xhr, status, err) {
            if(mask) wrapWindowByUnMask();

            //var domain = location.host.split('.')[0];
            var responseText = xhr.responseText;

            if(xhr.status == 500 || xhr.status == 404) {
                responseText = responseText.replace('{', '').replace('}', '').replace(/[\"]/g,'');
                var alertDoc = new DOMParser().parseFromString(responseText, "text/html");
                alert(alertDoc.body.innerHTML);
            } else {
                responseText = responseText.replace('{', '').replace('}', '').replace(/[\"]/g,'');
                responseText = responseText.substring(responseText.indexOf(':') + 1).trim();
                alert(responseText);
            }
            if (errorCallbackFunction != null && errorCallbackFunction != undefined && errorCallbackFunction != false) {
                errorCallbackFunction(); // User Function Call
            }
        }
        , beforeSubmit: function(formData, jqForm, options) {
            if(mask) wrapWindowByMask();
        }
    });
}