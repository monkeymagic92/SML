
// 테스트 용
function commonTest() {
    alert('common Test');
}

function fnSearch() {
    var args = arguments;

    var url = args[0];
    var frm = args[1];

    $(frm).ajax({
        type: 'post'
        , url: url
    })
}