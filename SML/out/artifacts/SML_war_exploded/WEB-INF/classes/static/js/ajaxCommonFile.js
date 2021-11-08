
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