
// 테스트 용
function commonTest() {
    alert('common Test');
}

function test() {

}

function fnSearch() {
    var args = arguments;

    var url = args[0];
    var frm = args[1];

    $(frm).ajaxSubmit({
        type: 'post'
        , url: url
    })
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

