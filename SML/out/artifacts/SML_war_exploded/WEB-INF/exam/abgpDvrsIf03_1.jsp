<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ducf.core.util.SessionUtil" %>
<jsp:directive.include file="/jsp/include/compage.jsp"/>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%
    //예산년도 올해로 초기화하기
    Calendar cal = Calendar.getInstance();
    String strYear = Integer.toString(cal.get(Calendar.YEAR));

    // 당일
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String dt = sdf.format(new Date()); //일시
%>
<c:set value='<%=strYear %>' var="setYear" />
<c:set value='<%=dt %>' var="setDt" />
<c:set value='<%=SessionUtil.get("POSI_DEPT_CD")%>' var="dept" />
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
    <style>
        #btnMulti {width:90px; height:28px; vertical-align: middle; cursor: pointer; margin-right: 2px; border:1px solid grey; font-weight: bold; color: grey; }
    </style>

    <jsp:directive.include file="/jsp/include/comhead.jsp"/>

    <link type="text/css" href="<c:url value="/js/select2/css/select2.css" />" rel="Stylesheet">
    <script type="text/javascript" src="<c:url value="/js/select2/js/select2.full.js" />"></script>

    <script type="text/javascript">

        /* 기본 정보 변수 선언 시작 (그리드가 여러개일 경우 변수끝에 번호를 붙여 추가) */
        var gridObj1;
        var searchUrl1;
        var saveUrl1;
        var saveUrl2;
        var DVRS_NO;
        var idLen = 1;
        /* 기본 정보 변수 선언 종료 */

        $(document).ready(function(){

            $("#DVRS_APLY_DEPT").select2({'language': "ko"});

            if("${selectOne.DVRS_NO}" != ""){
                $('#freeForm').find("#STATUS").val('U');
            }

            $("#searchDVRS_NO").val("${selectOne.DVRS_NO}");
            /* 기본 정보 정의 시작 */
            gridObj1 = $("#grid1"); //그리드 DIV 아이디 정의
            searchUrl1 = '<c:url value="/abg/abgpDvrsIf03/list.do" />'; //조회 URL 정의
            saveUrl1 = '<c:url value="/abg/abgpDvrsIf03/save.do" />'; //저장 URL 정의
            saveUrl2 = '<c:url value="/abg/abgpDvrsIf03/saveSub1.do" />'; //저장 URL 정의
            /* 기본 정보 정의 종료 */

            /*----- 그리드 옵션 초기 설정 시작 -----*/
            //그리드타이틀, 조회URL, 조회조건 폼 아이디, 페이징 아이디, 그리드 넓이(true인경우 autowidth 처리), 그리드 높이, 멀티선택 여부, 그리드 키 컬럼명
            gridInit(gridObj1, '전용감소', searchUrl1, '#searchForm', '#pager1', 1070, 400, true, 'RNUM', true, 1000, true, [100,15,20,1000], 'json');
            /*----- 그리드 옵션 초기 설정 종료 -----*/

            /*----- 컬럼 타이틀 설정 시작 -----*/
            gridOptions.colNamesInit('상태', 'NO','검색', '예산요구번호', '사업번호', '세부사업', '계정책임자(코드)', '계정책임자', '부서(코드)', '부서', '예산과목(코드)', '예산과목', '예산현액', '배정액', '원인액', '전용가액', '전용요구액', '증감여부', '전용코드', '전용상세코드', '전용구분코드');
            /*----- 컬럼 타이틀 설정 종료 -----*/

            gridOptions.colModelInit('STATUS', 'center', 50,true, false, true);

            gridOptions.colModelInit('RNUM', 'center', 50, true, true, false);

            //팝업아이콘
            gridOptions.colModelInit('BTN_POPUP', 'center', 40, false, false, false);
            gridOptions.colModelFormatter(getbutton);

            //예산요구번호
            gridOptions.colModelInit('BUDG_DMD_NO', 'center', 100, false, true, false);

            //사업번호
            gridOptions.colModelInit('BIZ_NO', 'center', 80, true, true, false);

            //세부사업
            gridOptions.colModelInit('BIZ_NM', 'center', 200, false, true, false);

            //계정책임자(코드)
            gridOptions.colModelInit('ACCT_CHAR_EMPNO', 'center', 80, true, true, false);

            //계정책임자
            gridOptions.colModelInit('ACCT_CHAR_EMPNM', 'center', 80, false, true, false);

            //부서(코드)
            gridOptions.colModelInit('DEPT_CD', 'center', 80, true, true, false);

            //부서
            gridOptions.colModelInit('DEPT_NM', 'center', 80, false, true, false);

            //예산과목(코드)
            gridOptions.colModelInit('BUDG_SBJT_CD', 'center', 50, true, true, false);

            //예산과목
            gridOptions.colModelInit('BUDG_SBJT_NM', 'center', 100, false, true, false);

            //예산현액
            gridOptions.colModelInit('TT_AMT', 'right', 90, false, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            //집행액
            gridOptions.colModelInit('ASGN_ACCP_AMT', 'right', 70, false, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            //원인액
            gridOptions.colModelInit('EXP_CAUS_AMT', 'right', 70, false, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            //잔액
            gridOptions.colModelInit('BALANCE_AMT', 'right', 70, false, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            //전용요구액
            gridOptions.colModelInit('DVRS_AMT', 'right', 70, false, false, true);
            gridOptions.colModelEdit('text', {required:true},
                {
                    maxlength:'100',
                    dataInit: function(element) {
                        fnFormatMask(element, "integer");
                    },
                    dataEvents:[{type:'keyup',fn:function(e){fnCheckAMT(e);}}]
                });
            gridOptions.colModelFormatter('integer');
            gridOptions.colModelEtc('column_ps', null);

            //증감여부
            gridOptions.colModelInit('INDE_YN', 'right', 50, true, false, false);

            //전용코드
            gridOptions.colModelInit('DVRS_NO', 'right', 50, true, false, false);

            //전용상세코드
            gridOptions.colModelInit('DVRS_DETL_NO', 'center', 50, true, false, false);

            //전용구분코드
            gridOptions.colModelInit('DVRS_DCD', 'center', 0, true, false, false);

            //컬럼 설정 완료
            gridOptions.colModelComplete();
            gridOptions.gridComplete = function(){fnComplate();};
            gridOptions.footerrow = true;
            gridOptions.userDataOnFooter = true;

            /*----- 컬럼 옵션 설정 종료 -----*/

            /*----- jqGrid 객체에 그리드 옵션 설정 시작 -----*/
            gridOptions.onSelectRow = function(id){
                var status = gridObj1.find("#"+id+"_STATUS").val();
                var status2 = gridObj1.getCell(id, "STATUS");
                //if(status == "C" || status2 == "C"){
                //	fnSelectRow(id, gridObj1, true);
                //}
                <c:if test="${dto.editable == 'true'}">
                fnSelectRow(id, gridObj1, true);
                </c:if>

            };
            //gridOptions.gridComplete = function(){fnSearchGrid2();fnSearchGrid5();};
            gridObj1.jqGrid(gridOptions);

            //그리드 설정 완료
            gridComplete();
            /*----- jqGrid 객체에 그리드 옵션 설정 종료 -----*/

            /********************** 1번 그리드 설정 종료 *********************/


            /********************** 버튼 클릭 이벤트 처리 시작 *********************/
            // 다중선택 버튼 클릭
            $("#btnMulti").click(function() {
                fnMultiChk();
            });

            $("#btnNew1").click(function() {
                var newData = "{STATUS:'C', INDE_YN:'N'}";
                $("#searchMultiChkYn").val('N');
                fnNew(gridObj1, newData);
            });

            $("#btnSave1").click(function() {
                gridObj1RowCheck();
            });

            $("#btnDel1").click(function() {
                fnDelDirect(gridObj1, saveUrl2);
            });

            $("#btnSearch1").click(function() {
                fnSearch(gridObj1);
            });

            $("#btnOk").click(function() {
                //승인
                if(confirm('승인 하시겠습니까?')) {

                    $("#selectedDVRS_NO").val($("#DVRS_NO").val());
                    $("#searchForm").ajaxSubmit({
                        dataType: 'json'
                        , url: '<c:url value="/abg/abgeDvrsAplyUser/chkDvrsMunusAmt.do" />'
                        , success: function(data){

                            var array = data.list;
                            var cntArray = 0;

                            $.each(array, function() {
                                if(this.CNT > 0) {
                                    cntArray++;
                                }
                            });

                            if(cntArray > 0){
                                alert("전용감액이 전용가능액을 초과했습니다.");
                                return true;
                            }else{
                                $("#PROG_ST_DCD").val("CMN28.06");
                                //gridObj1RowCheck();
                                fnFreeformSave(saveUrl1, '#freeForm', saveSuccess, '');
                            }
                        }
                        , error: function(data, status, err) {
                            alert('시스템 작업중입니다.['+data.status+']');
                        }
                    });
                }
            });
            /********************** 버튼 클릭 이벤트 처리 종료 *********************/

            if("${selectOne.DVRS_APLY_DTTM }" != "") {
                fnDatepicker($("#DVRS_APLY_DTTM"));
            } else {
                fnDatepicker($("#DVRS_APLY_DTTM"), true);
            }

            var t1 = gridObj1.getDataIDs();
            for(var i=0; i < t1.length; i++){
                console.log(getGridCell(gridObj1,""))
            }

        });

        /********************** Javascript 변수 및 함수 설정 시작 ********************/
        function fnComplate(){
            var tt_amt = (gridObj1.getCol('TT_AMT', false, 'sum'));
            var dvrs_amt = (gridObj1.getCol('DVRS_AMT', false, 'sum'));
            gridObj1.jqGrid('footerData', 'set', {BUDG_SBJT_NM:'합계', TT_AMT:tt_amt, DVRS_AMT:dvrs_amt});
        }

        function getbutton(cellvalue, options, rowObject){
            var btns = "";
            var rowId = options.rowId;

            if(rowObject.STATUS == "C"){
                btns = '<a href="javascript:popAbgpExecBudg(\''+rowId+'\');"><img src="<c:url value="/images/btn_pop.gif" />" border=0 /></a>';
            }
            else{
                btns = '<img src="<c:url value="/images/btn_pop_dis.gif" />" border=0 />';
            }
            return btns;
        }

        // '다중선택' 버튼 클릭시 '실행예산' 팝업창에서 체크박스를 활성화 한다
        function fnMultiChk() {
            var rowId = 'C1';
            $("#searchMultiChkYn").val('Y');  // Y = '실행예산' 팝업창에서 그리드 다중선택 체크박스 활성화
            popAbgpExecBudg(rowId);
        }

        function popAbgpExecBudg(rowId){
            //신규 실행예산 팝업 사업담당자만 나옴
            var s_url = '<c:url value="/abg/abgpNewExecBudg/popup.do" />';
            var pyear = '${setYear}';
            pyear = '${setYear}, '+(Number('${setYear}')-1).toString(); //16.02.17 전용일괄증가 년도조건 이전년도 추가
            var rnd_yn = 'N'; //전용일괄감소는 행정에만있음.

            fnOpenPopup("search_popAbgpExecBudg", "about:blank", 1024, 500);

            $("#popupForm").find("#rowId").val(rowId);
            $("#popupForm").find("#gridObj").val("grid1");
            $("#popupForm").find("#p_year").val(pyear);
            $("#popupForm").find("#rnd_yn").val(rnd_yn);
            $("#popupForm").find("#searchNotPermissionDeptYn").val("Y");
            $("#popupForm").find("#searchMultiChkYn").val();
            $("#popupForm").attr("target", "search_popAbgpExecBudg");
            $("#popupForm").attr("action", s_url);
            $("#popupForm").setAuthSync(); //권한 동기화
            $("#popupForm").submit();
        }



        // '실행예산' 팝업창에서 단일, 다중 선택처리  jytest
        function setArrExecBudg(gridObj, rowId ,selectedRowArr, multiChkYn) {

            setStatusForPopup(rowId);

            var grid = $("#" + gridObj);
            var newData = "{STATUS:'C', INDE_YN:'N'}";	// INDE_YN : N = 감소, Y = 증가

            // 그리드에 데이터가 있을경우 추가적으로 다중선택을 눌렀을때 현재 마지막 row의 + 1값을 가져온다
            var ids = gridObj1.getDataIDs();
            idLen = ids.length + 1;

            var x = idLen;

            // 다중선택 = Y, 단일선택 = N
            if (multiChkYn == 'Y') {

                for (var i = 1; i < selectedRowArr.length + 1; i++) {

                    // 다중선택) 기존에 그리드에 데이터가있을경우 반복문 변수 = X
                    if( x > 1) {

                        fnNew(gridObj1, newData);	// C1, C2, C3... 행(row)을 추가하기위한 fnNew함수

                        grid.setCell('C' + x, 'BUDG_DMD_NO', selectedRowArr[i-1]['BUDG_DMD_NO']);
                        grid.setCell('C' + x, 'BIZ_NO', selectedRowArr[i-1]['BIZ_NO']);
                        grid.setCell('C' + x, 'BIZ_NM', selectedRowArr[i-1]['BIZ_NM']);
                        grid.setCell('C' + x, 'BUDG_SBJT_CD', selectedRowArr[i-1]['BUDG_SBJT_CD_3']);
                        grid.setCell('C' + x, 'BUDG_SBJT_NM', selectedRowArr[i-1]['BUDG_SBJT_NM']);
                        grid.setCell('C' + x, 'TT_AMT', selectedRowArr[i-1]['TT_AMT']);
                        grid.setCell('C' + x, 'ASGN_ACCP_AMT', selectedRowArr[i-1]['ASGN_ACCP_AMT']);
                        grid.setCell('C' + x, 'ACCT_CHAR_EMPNO', selectedRowArr[i-1]['ACCT_CHAR_EMPNO']);
                        grid.setCell('C' + x, 'ACCT_CHAR_EMPNM', selectedRowArr[i-1]['ACCT_CHAR_EMPNM']);
                        grid.setCell('C' + x, 'BALANCE_AMT', selectedRowArr[i-1]['AMT2']);
                        x++;

                        // 다중선택) 기존에 그리드에 데이터가 없을 경우 반복문 변수 = i
                    } else {
                        fnNew(gridObj1, newData);	// C1, C2, C3... 행(row)을 추가하기위한 fnNew함수

                        grid.setCell('C' + i, 'BUDG_DMD_NO', selectedRowArr[i-1]['BUDG_DMD_NO']);
                        grid.setCell('C' + i, 'BIZ_NO', selectedRowArr[i-1]['BIZ_NO']);
                        grid.setCell('C' + i, 'BIZ_NM', selectedRowArr[i-1]['BIZ_NM']);
                        grid.setCell('C' + i, 'BUDG_SBJT_CD', selectedRowArr[i-1]['BUDG_SBJT_CD_3']);
                        grid.setCell('C' + i, 'BUDG_SBJT_NM', selectedRowArr[i-1]['BUDG_SBJT_NM']);
                        grid.setCell('C' + i, 'TT_AMT', selectedRowArr[i-1]['TT_AMT']);
                        grid.setCell('C' + i, 'ASGN_ACCP_AMT', selectedRowArr[i-1]['ASGN_ACCP_AMT']);
                        grid.setCell('C' + i, 'ACCT_CHAR_EMPNO', selectedRowArr[i-1]['ACCT_CHAR_EMPNO']);
                        grid.setCell('C' + i, 'ACCT_CHAR_EMPNM', selectedRowArr[i-1]['ACCT_CHAR_EMPNM']);
                        grid.setCell('C' + i, 'BALANCE_AMT', selectedRowArr[i-1]['AMT2']);
                    }
                }

                // '다중선택' 이후 1개의 행을 수정할경우 '단일선택'으로 설정하기위해 'N'으로 변경
                $("#popupForm").find("#searchMultiChkYn").val('N');

            } else {
                grid.setCell(rowId, 'BUDG_DMD_NO', selectedRowArr[0]['BUDG_DMD_NO']);
                grid.setCell(rowId, 'BIZ_NO', selectedRowArr[0]['BIZ_NO']);
                grid.setCell(rowId, 'BIZ_NM', selectedRowArr[0]['BIZ_NM']);
                grid.setCell(rowId, 'BUDG_SBJT_CD', selectedRowArr[0]['BUDG_SBJT_CD_3']);
                grid.setCell(rowId, 'BUDG_SBJT_NM', selectedRowArr[0]['BUDG_SBJT_NM']);
                grid.setCell(rowId, 'TT_AMT', selectedRowArr[0]['TT_AMT']);
                grid.setCell(rowId, 'ASGN_ACCP_AMT', selectedRowArr[0]['ASGN_ACCP_AMT']);
                grid.setCell(rowId, 'ACCT_CHAR_EMPNO', selectedRowArr[0]['ACCT_CHAR_EMPNO']);
                grid.setCell(rowId, 'ACCT_CHAR_EMPNM', selectedRowArr[0]['ACCT_CHAR_EMPNM']);
                grid.setCell(rowId, 'BALANCE_AMT', selectedRowArr[0]['AMT2']);
            }

            var id = gridObj1.getDataIDs();
            var selectedBUDG_DMD_NO = "";
            for(var i=0; i<selectedRowArr.length+1; i++){
                var budg_dmd_no = gridObj1.getCell(id[i], "BUDG_DMD_NO");

                if(budg_dmd_no != ""){
                    selectedBUDG_DMD_NO += budg_dmd_no+" ,";
                }
            }
            $("#selectedBUDG_DMD_NO").val(selectedBUDG_DMD_NO.substring(0, selectedBUDG_DMD_NO.length -1));
            //fnSearch(gridObj3, null, null, '');

            console.log($("#selectedBUDG_DMD_NO").val());
        }

        function fnCheckAMT(e){
            var obj = $(e.target);
            var gridTbl = obj.closest('table.ui-jqgrid-btable');
            var row = obj.closest('tr.jqgrow');
            var id = row.attr('id');

            var BALANCE_AMT = Number(gridTbl.getCell(id, "BALANCE_AMT"));
            var BALANCE_AMT2 = Number(gridTbl.getCell(id, "TT_AMT")) - Number(gridTbl.getCell(id, "EXP_CAUS_AMT"));
            var DVRS_AMT = Math.abs(Number(gridTbl.find('#'+id+'_DVRS_AMT').val().replace(/,/gi,"")));
            var BUDG_SBJT_CD = gridTbl.getCell(id, "BUDG_SBJT_CD");
            if(BUDG_SBJT_CD != ''){
                if(BALANCE_AMT < DVRS_AMT){
                    alert("전용 요구액은 전용가액을 초과할 수 없습니다.");
                    gridTbl.find('#'+id+'_DVRS_AMT').val("0");
                    return;
                } else if(BALANCE_AMT2 < DVRS_AMT){
                    alert("전용가액보다 많은 원인행위가 잡혀 있습니다. 원인행위 종결 후 진행하십시오.");
                    gridTbl.find('#'+id+'_DVRS_AMT').val("0");
                    return;
                }
            }else{
                alert("예산과목을 먼저 선택하여 주십시오.");
                gridTbl.find('#'+id+'_DVRS_AMT').val("0");
                return;
            }
            gridTbl.find('#'+id+'_DVRS_AMT').val(DVRS_AMT);
        }

        function gridObj1RowCheck(){
            var rows = gridObj1.getDataIDs();
            var biz_no = "0";
            var tmp_biz_no = "0";
            var BUDG_SBJT_CD = "0";
            var tmp_BUDG_SBJT_CD = "0";
            $("#MINUS_DVRS_AMT").val("0");
            var plus_DVRS_AMT = 0;

            var ids = gridObj1.getDataIDs();
            var checkBUDG_DMD_NO = true;
            for(var i=0; i<ids.length; i++){
                var budg_dmd_no = gridObj1.getCell(ids[i], "BUDG_DMD_NO");
                if(budg_dmd_no == ""){
                    checkBUDG_DMD_NO = false;
                }
            }

            if(checkBUDG_DMD_NO) {
                if(rows.length > 0){
                    var DECR_AMT_RESN = $("#DECR_AMT_RESN").val();

                    if(DECR_AMT_RESN == ""){
                        alert("감액사유를 입력하여 주십시오.");
                        $("#DECR_AMT_RESN").focus();
                    } else{
                        checkDvrsBudg();
                    }
                }else{
                    alert("전용감소 항목을 입력 하여 주십시오.");
                }
            } else {
                alert("전용감소 항목의 예산요구를 선택하세요.");
            }
        }

        function SaveFreeForm(){
            $("body").append('<div id="windowByMask"><img src="'+contextPath+'/images/viewLoading.gif" /></div>');
            wrapWindowByMask();
            fnFreeformSave(saveUrl1, '#freeForm', saveGrid1, '');
        }

        function saveGrid1(data){

            var chk = 0;

            DVRS_NO = data.DVRS_NO;
            var array = data.DVRS_NO;
            if(array != ""){
                var rows = gridObj1.getDataIDs();
                fnFormatUnmask(gridObj1);
                for(var i=0; i<rows.length;i++){
                    gridObj1.saveRow(rows[i], false, 'clientArray');
                    var status = gridObj1.getCell(rows[i], "STATUS");
                    var DVRS_AMT = gridObj1.getCell(rows[i], "DVRS_AMT");

                    if(status == "U") {
                        if(DVRS_AMT > 0) {
                            DVRS_AMT *= -1;
                            //alert(DVRS_AMT);
                        }
                        gridObj1.setCell(rows[i], "DVRS_AMT", DVRS_AMT);
                    }

                    gridObj1.setCell(rows[i], "DVRS_NO", array);

                    if(status == "C" || status == "U"){
                        chk++;
                    }
                }

                if(chk == 0){
                    alert("저장할 내용이 없습니다.");
                    wrapWindowByUnMask();

                    return false;
                }

                fnSave(gridObj1, saveUrl2, null, null, saveSuccess, '', false);
            }

        }

        function saveSuccess(data){
            opener.searchDEPT_OnChange();
            alert("정상적으로 저장되었습니다.");
            if($("#PROG_ST_DCD").val() == "CMN28.06") {
                self.close();
            } else {
                var formString = '';
                formString += "<form id='reloadForm' NAME='reloadForm' action='"+contextPath+"/abg/abgpDvrsIf03/popup2.do' method='post'>" ;
                formString += "<input type='hidden' name='DVRS_NO' value="+DVRS_NO+">" ;
                if($("#PROG_ST_DCD").val() != "CMN28.06") formString += "<input type='hidden' name='editable' value='true'>" ;
                formString += "</form>" ;

                $("#reloadForm").remove();
                $("body").append(formString);
                $("#reloadForm").setAuthSync();//권한동기화
                $("#reloadForm").submit();
            }

            wrapWindowByUnMask();
        }

        //전용에 사용되고있는 예산요구번호
        function checkDvrsBudg(){

            var id = gridObj1.getDataIDs();
            var selectedBUDG_DMD_NO = "";
            for(var i=0; i<id.length; i++){
                var budg_dmd_no = gridObj1.getCell(id[i], "BUDG_DMD_NO");
                if(budg_dmd_no != ""){
                    selectedBUDG_DMD_NO += budg_dmd_no+" ,";
                }
            }

            var a = $("#selectedBUDG_DMD_NO").val(selectedBUDG_DMD_NO.substring(0, selectedBUDG_DMD_NO.length -1));

            $("#searchForm").ajaxSubmit({
                dataType: 'json'
                , url: '<c:url value="/abg/abgpDvrsAplyUserIf02/selectDvrsBudgDmd.do" />'
                , success: function(data){
                    var array = data.list;
                    var result = true;
                    var chkCnt = 0;
                    var dvrsBudgDmdNo = "";

                    $.each(array, function() {
                        if(this.CNT > 0){
                            dvrsBudgDmdNo = dvrsBudgDmdNo + this.BUDG_DMD_NO + ", ";
                            chkCnt++;
                        }
                    });

                    dvrsBudgDmdNo = dvrsBudgDmdNo.substring(0, dvrsBudgDmdNo.length-2);

                    if(chkCnt > 0){
                        alert("실행예산("+dvrsBudgDmdNo+")이 전용승인전입니다. 승인이후 선택해주십시오.");
                        result = false;
                    }

                    if(result) SaveFreeForm();

                }, error: function(data, status, err) {
                    alert('시스템 작업중입니다.['+data.status+']');
                }
            });
        }

        //팝업시 STATUS 값 수정
        function setStatusForPopup(rowId){
            //처음생성될때 수정모드가 켜있어서 이렇게 불러옴
            var status1 = $("#"+rowId+"_STATUS");
            //값을 바꾸거나 조회후 수정모드 꺼져있어 이렇게 불러옴
            var status2 = gridObj1.getCell(rowId, "STATUS");

            if(status1.val() == undefined){
                if(status2 == null || status2 == '')
                    gridObj1.setCell(rowId, "STATUS", "U");
                else if(status2 != "C")
                    gridObj1.setCell(rowId, "STATUS", "U");
            }else if(status1.val() != "C"){
                status1.val("U");
            }
        }

        /********************** Javascript 변수 및 함수 설정 종료 *********************/
    </script>
</head>
<body>
<!-- 공통 팝업 버튼 영역 시작 -->
<c:import url="/jsp/include/popbutton.jsp" charEncoding="utf-8">
    <c:param name="paramPopupTitle" value="예산전용(신청)" />
</c:import>
<!-- 공통 팝업 영역 종료 -->

<!-- 팝업 폼 시작 -->
<form id="popupForm" name="popupForm" method="post">
    <input type="hidden" id="rowId" name="rowId" value="" />
    <input type="hidden" id="gridObj" name="gridObj" value="" />
    <input type="hidden" id="p_year" name="p_year" value="" />
    <input type="hidden" id="rnd_yn" name="rnd_yn" value="" />
    <input type="hidden" id="searchNotPermissionDeptYn" name="searchNotPermissionDeptYn" value="" />
    <input type="hidden" id="searchBUDG_DMD_NO" name="searchBUDG_DMD_NO" value="" />
    <input type="hidden" id="searchBIZ_LRG" name="searchBIZ_LRG" value="ABG02.2" />
    <input type="hidden" id="searchMultiChkYn" name="searchMultiChkYn" value="" />
</form>
<!-- 팝업 폼 종료 -->

<!-- 데이터 조회조건 폼 시작 -->
<form id="searchForm" name="searchForm" method="post" >
    <!-- 조회조건 공통항목 시작  -->
    <jsp:directive.include file="/jsp/include/comsearchelement.jsp"/>
    <!-- 조회조건 공통항목 종료  -->
    <input type="hidden" id="searchDVRS_NO" name="searchDVRS_NO" value="${selectOne.DVRS_NO}" />
    <input type="hidden" id="selectedBUDG_DMD_NO" name="selectedBUDG_DMD_NO" /> <!-- 전자결재진행중인 예산체크용 -->
    <input type="hidden" id="searchRND_YN" name="searchRND_YN" value="N" /> <!-- 전자결재진행중인 예산체크용 -->
    <input type="hidden" id="selectedDVRS_NO" name="selectedDVRS_NO" /> <!-- 전용감액체크용 -->
    <input type="hidden" name="SUM_STRING" id="SUM_STRING"/>
</form>

<!-- 데이터 조회조건 폼 종료 -->

<!-- ********************** 일반 버튼 및 그리드 정의 시작 ********************** -->
<table border="0" cellpadding="0" cellspacing="0" style="width:100%;">
    <tr>
        <td>
            <!-- 공통 버튼 Include sdsd -->
            <table border="0" cellpadding="0" cellspacing="0"  style="width:100%;">
                <tr>
                    <td width="22%" class="content_title"><img src="<c:url value="/images/ico_snb_2.gif" />" width="9" height="9" />&nbsp;예산전용(신청)</td>
                    <td width="78%" style="text-align:right; padding-right:11px; padding-top:3px;">
                        <!--button-->
                        <button id="btnSearch1" class="btn_search" title="조회" type="submit" tabindex="1"><span class="blind">조회</span></button>
                        <c:if test="${dto.editable == 'true'}">
                            <button id="btnSave1" class="btn_save" title="저장" type="submit" tabindex="4"><span class="blind">저장</span></button>
                            <c:if test="${!empty selectOne.DVRS_NO}">
                                <input type="button" id="btnOk" name="btnOk" class="btn_bg2_red" title="승인" alt="승인" value="승인" >
                            </c:if>
                        </c:if>
                        <!--//button-->
                    </td>
                </tr>
            </table>
            <!-- 공통 버튼 Include -->
        </td>
    </tr>
    <tr>
        <td>
            <form id="freeForm" name="freeForm" method="post" enctype="multipart/form-data">
                <!-- 프리폼 공통항목 시작  -->
                <jsp:directive.include file="/jsp/include/comfreeformelement.jsp" />
                <!-- 프리폼 공통항목 종료  -->
                <input type="hidden" id="DVRS_NO" name="DVRS_NO" value="${selectOne.DVRS_NO}" />

                <!-- 연구가아니면 비수탁, 비R&D (전용일괄신청은 일반행정만 씀) -->
                <input type="hidden" id="BIZ_DIV" 		name="BIZ_DIV" 		value="ABG18.20" />
                <input type="hidden" id="BIZ_LRG_DCD" 	name="BIZ_LRG_DCD" 	value="ABG02.2" />
                <input type="hidden" id="DVRS_DCD" 		name="DVRS_DCD" 	value="ABG22.03" />


                <table width="100%" class="freeform_tbl_header">
                    <tr>
                        <td colspan="10" class="freeform_tbl_hRight">
                            <div style="color:#ff0000; text-align:right;">"승인" 버튼을 클릭 하시면 전용 이력이 반영됩니다.</div>
                        </td>
                    </tr>
                    <tr>
                        <td width="126" align="center" class="freeform_tbl_hLeft">신청일자&nbsp;</td>
                        <td width="80" class="freeform_tbl_hRight">
                            <input type="text" id="DVRS_APLY_DTTM" name="DVRS_APLY_DTTM" class="ctl_input" value="${selectOne.DVRS_APLY_DTTM }" />
                        </td>
                        <td width="126" align="center" class="freeform_tbl_hLeft">부서명&nbsp;</td>
                        <td width="100" class="freeform_tbl_hRight">
                            <c:if test="${!empty selectOne.PROG_ST_DCD}" >
                                <ctlcmn:select idName='DVRS_APLY_DEPT' tabIndex='2'
                                               directQuery="SELECT DEPT_CD AS CODE, KOR_DEPT_NM AS NAME FROM CMN_DEPT_C WHERE DEPT_CD = '${selectOne.DVRS_APLY_DEPT }' ORDER BY KOR_DEPT_NM"  />
                            </c:if>
                            <c:if test="${empty selectOne.PROG_ST_DCD}" >
                                <ctlcmn:select idName='DVRS_APLY_DEPT' tabIndex='2' initVal='${dept }'
                                               directQuery="SELECT DEPT_CD AS CODE, KOR_DEPT_NM AS NAME FROM CMN_DEPT_C WHERE DEPT_CD IN (${selectedDept})
						    OR DEPT_CD IN (${ABG_DEPT }) ORDER BY KOR_DEPT_NM"  />
                            </c:if>
                        </td>
                        <td width="126" align="center" class="freeform_tbl_hLeft">진행상태&nbsp;</td>
                        <td width="100" class="freeform_tbl_hRight">
                            <%-- <c:if test="${!empty selectOne.PROG_ST_DCD}">
                            <ctlcmn:select codeCol="INTG_CD" nameCol="KOR_CD_NM"
                                idName="PROG_ST_DCD" tableNm="CMN_CMN_C" whereCol1='CD_DIV' whereVal1='CMN28'
                                whereCol2='DETL_CD' whereVal2='00000' whereCompare2='!='
                                whereCol3='INTG_CD' whereVal3='${selectOne.APPG_ST_DCD }' whereCompare3='='
                                sortCol="INTG_CD" sortType="ASC"
                                 >
                            </ctlcmn:select>
                            </c:if>

                            <c:if test="${empty selectOne.PROG_ST_DCD}" >
                                <ctlcmn:select codeCol="INTG_CD" nameCol="KOR_CD_NM"
                                idName="PROG_ST_DCD" tableNm="CMN_CMN_C" whereCol1='CD_DIV' whereVal1='CMN28'
                                whereCol2='DETL_CD' whereVal2='00000' whereCompare2='!='
                                whereCol3='INTG_CD' whereVal3='CMN28.01' whereCompare3='='
                                sortCol="INTG_CD" sortType="ASC"
                                 >
                            </ctlcmn:select>
                            </c:if> --%>

                            <ctlcmn:select idName='PROG_ST_DCD' tabIndex='2' initVal='${selectOne.APPG_ST_DCD }'
                                           directQuery="SELECT INTG_CD AS CODE, KOR_CD_NM AS NAME FROM CMN_CMN_C WHERE CD_DIV = 'CMN28' AND DETL_CD != '00000' AND INTG_CD IN ('CMN28.01', 'CMN28.06')"  />
                        </td>
                        <td width="126" align="center" class="freeform_tbl_hLeft">전용상세&nbsp;</td>
                        <td width="100" class="freeform_tbl_hRight">
                            <ctlcmn:select idName='DVRS_DIV_DETL' tabIndex='2' initVal='${selectOne.DVRS_DIV_DETL }'
                                           directQuery="SELECT INTG_CD AS CODE, KOR_CD_NM AS NAME FROM CMN_CMN_C WHERE CD_DIV = 'ABG21' AND DETL_CD != '00000' AND INTG_CD IN ('ABG21.01', 'ABG21.02')"  />
                        </td>
                        <td width="126" align="center" class="freeform_tbl_hLeft">신청자&nbsp;</td>
                        <td width="80" class="freeform_tbl_hRight">

                            <c:if test="${!empty selectOne.DVRS_APLY_EMPNO}" >
                                <span>${selectOne.DVRS_APLY_EMPNM}</span>
                            </c:if>
                            <c:if test="${empty selectOne.DVRS_APLY_EMPNO	}" >
                                <span><%=SessionUtil.get("KOR_REL_PSN_NM") %></span>
                                <input type="hidden" id="DVRS_APLY_EMPNO" name="DVRS_APLY_EMPNO" value="<%=SessionUtil.get("STTS_NO") %>" class="ctl_input" />
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="126" align="center" class="freeform_tbl_hLeft">감액사유&nbsp;</td>
                        <td width="*" class="freeform_tbl_hRight" colspan="3">
                            <input type="hidden" id="DVRS_RESN" name="DVRS_RESN" value=" " />
                            <textarea id="DECR_AMT_RESN" name="DECR_AMT_RESN" rows="6" style="width:99%;" class="ctl_textarea">${selectOne.DECR_AMT_RESN }</textarea>
                        </td>
                        <td width="126" align="center" class="freeform_tbl_hLeft">승인사유&nbsp;</td>
                        <td width="*" class="freeform_tbl_hRight" colspan="5">
                            <textarea id="ACCP_RESN" name="ACCP_RESN" rows="6" style="width:99%;" class="ctl_textarea">${selectOne.ACCP_RESN }</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="hidden" id="PLUS_DVRS_AMT" value="0" />
                            <input type="hidden" id="MINUS_DVRS_AMT" value="0" />
                            <input type="hidden" id="gridObj1BIZ_NO" />
                            <input type="hidden" id="gridObj1BUDG_SBJT_CD" />
                            <input type="hidden" id="gridObj2BIZ_NO" />
                            <input type="hidden" id="gridObj2BUDG_SBJT_CD" />
                        </td>
                    </tr>

                </table>
            </form>
        </td>
    </tr>
    <tr>
        <td>
            <!-- 공통 버튼 Include    -->
            <table border="0" cellpadding="0" cellspacing="0"  style="width:100%;" class="freeform_tbl_header">
                <tr>
                    <TD width="22%" class="content_title"><img src="<c:url value="/images/ico_snb_2.gif" />" width="9" height="9" />&nbsp;전용감소</TD>

                    <!--
                    <td width="22%" style="text-align:left; padding-left:1px; padding-top:3px; margin-left:200px;">
                        <p style="margin-bottom:1px; background-color: #f0f7fb; border-left: solid 4px #3498db; overflow: hidden; padding: 6px; line-height:20px;">
                            '다중선택'시 기존데이터들은 <u style="font-weight: bold;">삭제</u> 됩니다.<br>
                        </p>
                    </td>
                    -->

                    <td width="78%" style="text-align:right; padding-right:11px; padding-top:3px;">
                        <!--button-->
                        <c:if test="${dto.editable == 'true'}">
                            <button id="btnMulti" title="다중선택" type="submit" tabindex="1"><span>다중선택</span></button>
                            <button id="btnNew1" class="btn_new" title="신규" type="submit" tabindex="2"><span class="blind">신규</span></button>
                            <button id="btnDel1" class="btn_del" title="삭제" type="submit" tabindex="3"><span class="blind">삭제</span></button>
                        </c:if>
                        <!--//button-->
                    </td>
                </tr>
            </table>
            <!-- 공통 버튼 Include -->
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0"  style="width:100%;">
                <tr>
                    <td colspan="4" class="freeform_tbl_hLeft">
                        <span class="ctl_label">『전용감소 '-'』수지구분이 수입일 경우 1천 단위 이하 자동 절사, 지출일 경우 1천단위 이하 자동 절상됩니다.</span>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <div id="jqgrid1">
                <table id="grid1" class="scroll"></table> <!-- 테이블 영역 -->
                <div id="pager1"></div> <!-- 페이징 영역 -->
            </div>
        </td>
    </tr>
</table>
<!-- ********************** 일반 버튼 및 그리드 정의 종료 ********************** -->
<!-- 그리드 CUD 폼 시작 -->
<jsp:directive.include file="/jsp/include/comgridform.jsp"/>
<!-- 그리드 CUD 폼 종료 -->

</body>
</html>