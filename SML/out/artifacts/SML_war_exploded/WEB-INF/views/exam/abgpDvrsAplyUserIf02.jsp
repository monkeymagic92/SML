<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="ducf.core.util.SessionUtil" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<jsp:directive.include file="/jsp/include/compage.jsp"/>
<%
    //예산년도 올해로 초기화하기
    Calendar cal = Calendar.getInstance();
    String strYear = Integer.toString(cal.get(Calendar.YEAR));
    //당일
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    String dt = sdf.format(new Date()); //일시

%>
<c:set value='<%=strYear %>' var="setYear" />
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>
        #btnMulti {width:90px; height:28px; vertical-align: middle; cursor: pointer; margin-right: 2px; border:1px solid grey; font-weight: bold; color: grey; }
    </style>

    <jsp:directive.include file="/jsp/include/comhead.jsp"/>
    <jsp:directive.include file="/jsp/include/apshead.jsp"/>

    <!-- 헤더 인클루드 -->
    <c:if test="${dto.mode ne 'ifrm'}" > <!-- 연구비 전용 화면 개선 -->
        <jsp:directive.include file="/jsp/adm/abg/abgpDvrsAplyUserHeader.jsp"/>
    </c:if>

    <c:if test="${dto.mode eq 'ifrm'}" > <!-- 연구비 전용 화면 개선 -->
        <style>
            #jqgrid1 {margin:0px;}
            #jqgrid2 {margin:0px; margin-top:25px;}
        </style>
    </c:if>

    <script type="text/javascript">

        /* 기본 정보 변수 선언 시작 (그리드가 여러개일 경우 변수끝에 번호를 붙여 추가) */
        var gridObj1;
        var saveUrl1;
        var searchUrl1;
        var gridObj2;
        var saveUrl2;
        var searchUrl2;
        var freeformUrl;
        var rnd_display;
        var idLen = 1;
        /* 기본 정보 변수 선언 종료 */

        $(document).ready(function(){
            /* 기본 정보 정의 시작 */
            gridObj1 = $("#grid1"); //그리드 DIV 아이디 정의ServiceCall
            gridObj2 = $("#grid2"); //그리드 DIV 아이디 정의
            searchUrl1 = '<c:url value="/abg/abgpDvrsAplyUserIf02/list.do" />';
            searchUrl2 = '<c:url value="/abg/abgpDvrsAplyUserIf02/listSub1.do" />';
            saveUrl1 = '<c:url value="/abg/abgpDvrsAplyUserIf02/save.do" />'; //저장 URL 정의
            /* 기본 정보 정의 종료 */

            //넘겨받은값 세팅
            //$("#freeForm").find($("#STATUS")).val("${dto.STATUS}");

// 그리드 출력 여부(false가 출력)

            // cch
            if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y") {
                rnd_display = false;
            } else {
                rnd_display = true;
            }

            /*----- 그리드 옵션 초기 설정 시작 -----*/
            //그리드타이틀, 조회URL, 조회조건 폼 아이디, 페이징 아이디, 그리드 넓이(true인경우 autowidth 처리), 그리드 높이, 멀티선택 여부, 그리드 키 컬럼명
            gridInit(gridObj1, '감액정보임력', searchUrl1, '#searchForm', '#pager1', true, 143, true, 'RNUM', true, 10, true, [10, 20, 50, 100], 'json');
            /*----- 그리드 옵션 초기 설정 종료 -----*/

            /*----- 컬럼 타이틀 설정 시작 -----*/
            gridOptions.colNamesInit(
                "<spring:message code='AbgeDvrsAplyUser.grid.11' text= '상태' />",
                "<spring:message code='AbgeDvrsAplyUser.grid.12' text= 'No' />",
                "<spring:message code='AbgeDvrsAplyUser.grid.28' text= '검색' />",
                "<spring:message code='AbgeDvrsAplyUser.grid.30' text= '사업구분' />",
                '사업대분류',

                '사업중분류',
                '사업중분류',
                '사업소분류',
                '사업소분류',
                '사업세분류',
                '사업세세분류',

                "<spring:message code='AbgeDvrsAplyUser.grid.32' text= '사업번호' />",
                "<spring:message code='AbgeDvrsAplyUser.grid.33' text= '사업명' />",
                '계정책임자(코드)',
                "<spring:message code='AbgeDvrsAplyUser.grid.34' text= '계정책임자' />",
                '예산과목(코드)',
                "<spring:message code='AbgeDvrsAplyUser.grid.35' text= '예산과목' />",
                '예산현액',
                "<spring:message code='AbgeDvrsAplyUser.grid.37' text= '배정액' />",
                "<spring:message code='AbgeDvrsAplyUser.grid.38' text= '원인액' />",
                '배정률',
                '원인행위',
                '지급신청',
                '전용가액',
                '전용요구액',
                '전용후예산',
                "<spring:message code='AbgeDvrsAplyUser.grid.42' text= '현액' />",
                '증감여부',
                '전용코드',
                '전용상세코드',
                '학생인건비여부',
                "<spring:message code='AbgeDvrsAplyUser.grid.29' text= '예산요구번호' />",
                '예산합계제외여부'
            );
            /*----- 컬럼 타이틀 설정 종료 -----*/

            gridOptions.colModelInit('STATUS', 'center', 50, true, false, true);

            gridOptions.colModelInit('RNUM', 'center', 30, false, true, false);

            //팝업아이콘
            gridOptions.colModelInit('BTN_POPUP', 'center', 30, false, false, false);
            gridOptions.colModelFormatter(getbutton);

            //사업구분 [ABG18]
            var selectBIZ_DIV_DCD = "<ctlcmn:selectdata resType='jqgrid' tableNm='CMN_CMN_C' codeCol='INTG_CD' nameCol='KOR_CD_NM' whereCol1='CD_DIV' whereVal1='ABG18' whereCompare1='=' whereCol2='DETL_CD' whereVal2='00000' whereCompare2='!=' sortCol='PRN_ORD' sortType='ASC'/>";
            gridOptions.colModelInit('BIZ_DIV', 'left', 80, true, false, false);
            gridOptions.colModelEdit('select', null,
                {
                    value:selectBIZ_DIV_DCD
                });
            gridOptions.colModelFormatter('select', null);

            //사업대분류 [ABG02]
            var selectBIZ_LRG_DCD = "<ctlcmn:selectdata resType='jqgrid' tableNm='CMN_CMN_C' codeCol='INTG_CD' nameCol='KOR_CD_NM' whereCol1='CD_DIV' whereVal1='ABG02' whereCompare1='=' whereCol2='DETL_CD' whereVal2='00000' whereCompare2='!=' sortCol='PRN_ORD' sortType='ASC'/>";
            gridOptions.colModelInit('BIZ_LRG_DCD', 'left', 80, true, false, false);
            gridOptions.colModelEdit('select', null,
                {
                    value:selectBIZ_LRG_DCD
                });
            gridOptions.colModelFormatter('select', null);

            //사업중분류
            gridOptions.colModelInit('BIZ_MCD', 'center', 100, true, true, false);
            gridOptions.colModelInit('BIZ_MCD_NM', 'center', 100, true, true, false);
            //사업소분류
            gridOptions.colModelInit('BIZ_SCD', 'center', 100, true, true, false);
            if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y"){
                gridOptions.colModelInit('BIZ_SCD_NM', 'center', 80, true, true, false);
            }else{
                gridOptions.colModelInit('BIZ_SCD_NM', 'center', 80, false, true, false);
            }
            //사업세분류명
            gridOptions.colModelInit('BIZ_CLCD_NM', 'center', 100, true, true, false);
            //사업세세분류명
            gridOptions.colModelInit('BIZ_SDCD_NM', 'center', 100, true, true, false);

            //사업번호
            gridOptions.colModelInit('BIZ_NO', 'center', 80, true, true, false);

            //사업명
            gridOptions.colModelInit('BIZ_NM', 'center', 140, false, true, false);

            //계정책임자(코드)
            gridOptions.colModelInit('ACCT_CHAR_EMPNO', 'center', 80, true, true, false);

            //계정책임자
            gridOptions.colModelInit('ACCT_CHAR_EMPNM', 'center', 70, false, true, false);

            //예산과목(코드)
            gridOptions.colModelInit('BUDG_SBJT_CD', 'center', 50, true, true, false);

            //예산과목
            gridOptions.colModelInit('BUDG_SBJT_NM', 'center', 85, false, true, false);

            //예산현액
            gridOptions.colModelInit('TT_AMT', 'right', 60, false, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            //집행액
            gridOptions.colModelInit('ASGN_ACCP_AMT', 'right', 60, false, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            //원인액
            gridOptions.colModelInit('EXP_CAUS_AMT', 'right', 60, false, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y"){
                //배정율
                gridOptions.colModelInit('ASGN_RATE', 'center', 50, false, true, false);
            }else{
                //배정율
                gridOptions.colModelInit('ASGN_RATE', 'center', 50, true, true, false);
            }

            // 원인행위
            gridOptions.colModelInit('EXP_CAUS_AMT1', 'right', 70, rnd_display, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});
            gridOptions.colModelEtc('column_ps', null, 'sum');

            // 지급신청
            gridOptions.colModelInit('EXP_SUM_AMT1', 'right', 70, rnd_display, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});
            gridOptions.colModelEtc('column_ps', null, 'sum');

            // 전용가액
            if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y") {
                // RND는 배정잔액
                gridOptions.colModelInit('ASGN_ACCP_AMT1', 'right', 70, false, true, false);
                gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});
            } else {
                // 비RND는 예산잔액
                gridOptions.colModelInit('BALANCE_AMT', 'right', 70, false, true, false);
                gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});
            }

            //전용요구액
            gridOptions.colModelInit('DVRS_AMT', 'right', 60, false, false, true);
            gridOptions.colModelEdit('text', {required:true},
                {
                    maxlength:'100',
                    dataInit: function(element) {
                        fnFormatMask(element, "integer");
                    },
                    dataEvents:[{type:'keyup',fn:function(e){fnCheckAMT(e);}},
                        {type:'focusout',fn:function(e){fnMinusAMT(e);}}]
                });
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});
            gridOptions.colModelEtc('column_ps', null, 'sum');

            //전용 후 예산
            gridOptions.colModelInit('AF_AMT', 'right', 60, false, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            //현액
            gridOptions.colModelInit('CURR_AMT', 'right', 60, true, true, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            //증감여부
            gridOptions.colModelInit('INDE_YN', 'right', 50, true, false, false);

            //전용코드
            gridOptions.colModelInit('DVRS_NO', 'right', 50, true, false, false);

            //전용상세코드
            gridOptions.colModelInit('DVRS_DETL_NO', 'center', 50, true, false, false);

            //학생인건비여부
            gridOptions.colModelInit('STUDT_LABCOST_YN', 'left', 150, true, false, false);

            //예산요구번호
            gridOptions.colModelInit('BUDG_DMD_NO', 'center', 100, false, true, false);
            gridOptions.colModelEdit('text', {required:true});

            //예산합계제외여부(연구행정에서 사용)
            gridOptions.colModelInit('EXCEPT_SUM_BUDG_YN', 'center', 0, true, false, false);

            //컬럼 설정 완료
            gridOptions.colModelComplete();

            /*----- 컬럼 옵션 설정 종료 -----*/

            /*----- jqGrid 객체에 그리드 옵션 설정 시작 -----*/
            gridOptions.onSelectRow = function(id){fnSelectRow(id, gridObj1, true);};
            gridOptions.gridComplete = function(){fnSearchGrid2(); fnComplate();};
            gridOptions.footerrow = true;
            gridOptions.userDataOnFooter = true;

            <!-- 2020.06.19 그리드 합계표기할 때 예산합계제외처리하여 표기(연구비전용대상에서 예외 예산) -->
            if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y") {

                gridOptions.loadComplete = function() {
                    $("tr.jqgroup", this).show();

                    var sum = $(this).getCol('DVRS_AMT', false, 'sum');

                    var array = gridObj1.getDataIDs();
                    $.each(array, function() {
                        var row = gridObj1.getRowData(this);

                        if(row.EXCEPT_SUM_BUDG_YN == 'Y') {
                            sum = sum - row.DVRS_AMT;
                        }
                    });

                    $(this).jqGrid('footerData', 'set', {RNUM:'합계', DVRS_AMT:sum});
                };
            }


            gridObj1.jqGrid(gridOptions);

            //그리드 설정 완료
            gridComplete();

            // cch
            if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y"){
                gridObj1.setGroupHeaders({
                    useColSpanStyle: true, //true시 그룹핑이 안되어진 컬럼 헤더에 대한 상하 병합 처리
                    groupHeaders:[
                        {startColumnName: 'EXP_CAUS_AMT1', numberOfColumns: 2, titleText: '잔액(원)'}
                    ]
                });
            }
            /*----- jqGrid 객체에 그리드 옵션 설정 종료 -----*/

            /*------------ 2번 그리드 설정 시작 -------------*/
            /*----- 그리드 옵션 초기 설정 시작 -----*/
            //그리드타이틀, 조회URL, 조회조건 폼 아이디, 페이징 아이디, 그리드 넓이(true인경우 autowidth 처리), 그리드 높이, 멀티선택 여부, 그리드 키 컬럼명
            gridInit(gridObj2, '기지출 집행액', searchUrl2, '#searchForm1', '#pager2', true, 173, true, 'RNUM', true, 10, true, [10, 20, 50, 100], 'json');
            /*----- 그리드 옵션 초기 설정 종료 -----*/

            /*----- 컬럼 타이틀 설정 시작 -----*/
            gridOptions.colNamesInit(
                "<spring:message code='AbgeDvrsAplyUser.grid.11' text= '상태' />",
                "<spring:message code='AbgeDvrsAplyUser.grid.12' text= 'No' />",
                "<spring:message code='AbgeDvrsAplyUser.grid.33' text= '사업명' />",
                "<spring:message code='AbgeDvrsAplyUser.grid.35' text= '예산과목' />",
                "<spring:message code='AbgeDvrsAplyUser.grid.47' text= '금액' />",
                '상세 내역(수량, 단가 등)');
            /*----- 컬럼 타이틀 설정 종료 -----*/

            gridOptions.colModelInit('STATUS', 'center', 50, true, false, true);

            gridOptions.colModelInit('RNUM', 'center', 50, true, true, false);

            //사업명
            gridOptions.colModelInit('BIZ_NM', 'center', 180, false, true, false);

            //예산과목
            gridOptions.colModelInit('BUDG_SBJT_NM_PATH', 'center', 180, false, true, false);

            //금액
            gridOptions.colModelInit('EXP_DETL_AMT', 'right', 70, false, false, false);
            gridOptions.colModelFormatter('number', {decimalPlaces:0,defaultValue:0});

            //상세 내역(수량, 단가 등)
            gridOptions.colModelInit('EXP_CTENT', 'left', 300, false, true, false);

            //컬럼 설정 완료
            gridOptions.colModelComplete();

            /*----- 컬럼 옵션 설정 종료 -----*/

            /*----- jqGrid 객체에 그리드 옵션 설정 시작 -----*/
            //gridOptions.onSelectRow = function(id){fnSelectRow(id, gridObj2);};
            gridObj2.jqGrid(gridOptions);

            //그리드 설정 완료
            gridComplete();
            /*----- jqGrid 객체에 그리드 옵션 설정 종료 -----*/

            /*------------ 2번 그리드 설정 종료 -------------*/

            /********************** 그리드 설정 종료 *********************/


            /********************** 버튼 클릭 이벤트 처리 시작 *********************/
            // 다중선택 버튼 클릭
            $("#btnMulti").click(function() {
                fnMultiChk();
            });

            //닫기 버튼 클릭
            $("#btnClose").click(function() {
                self.close();
                opener.fnSearch(gridObj1);
            });

            // 조회 버튼 클릭
            $("#btnSearch").click(function() {
                fnSearch(gridObj1);
            });

            // 신규 버튼 클릭
            $("#btnNew1").click(function() {
                if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y"){
                    var newData = "{STATUS:'C', INDE_YN:'N', DVRS_NO:'"+ $('#searchDVRS_NO').val() +"', BIZ_NM:'"+ $('#searchPRJT_NM', parent.document).val() +"'}";
                } else {
                    var newData = "{STATUS:'C', INDE_YN:'N', DVRS_NO:'"+ $('#searchDVRS_NO').val() +"'}";
                }
                $("#searchMultiChkYn").val('N');
                fnNew(gridObj1, newData);
            });

            // 저장 버튼 클릭
            $("#btnSave").click(function() {
                if('${dto.mode}' == 'ifrm') { //전용 화면 개선

                } else {
                    if($("#windowByMask").size()==0) $("body").append('<div id="windowByMask"><img src="'+contextPath+'/images/viewLoading.gif" /></div>');
                    wrapWindowByMask();
                }

                setTimeout(function() {
                    gridObj1RowCheck();
                }, 500);
            });

            //삭제 버튼 클릭
            $("#btnDel1").click(function() {
                //삭제시 다삭제하는 거면 이월신청 정보를 삭제하고 삭제해야됨
                var s_id = gridObj1.selarrrow();
                var a_id = gridObj1.getDataIDs();

                if(s_id.length == a_id.length && s_id.length != 0){
                    var c_cnt = 0;
                    for(var i=0; i<a_id.length; i++){

                        var status1 = gridObj1.find("#"+a_id[i]+"_STATUS").val();
                        var status2 = gridObj1.getCell(a_id[i], "STATUS");
                        //alert(status1+", "+status2);
                        if(status1 == "C" || status2 == "C") c_cnt = c_cnt+1;
                    }
                    //alert(a_id.length+", "+c_cnt);
                    if(a_id.length != c_cnt){
                        if(!confirm("전용증액도 같이 삭제됩니다 삭제 하시겠습니까?"))	return;
                    }
                }

                fnDelDirect(gridObj1, saveUrl1, null, null, saveCallBack);
            });

            //이전 버튼 클릭
            $("#btnRtn").click(function() {
                $("#step01").click();
            });

            //다음 버튼 클릭
            $("#btnNext").click(function() {
                $("#step03").click();
            });

            /********************** 공통버튼 및 IME 처리 시작 *********************/
            if("${dto.editable}" != 'false'){
                // 팝업 공통 버튼(조회, 신규, 저장, 삭제, 선택, 닫기, 초기화)
                fnPopButtonVisible2('Y','N','Y','N','N','Y','N');
            }else{
                // 팝업 공통 버튼(조회, 신규, 저장, 삭제, 선택, 닫기, 초기화)
                fnPopButtonVisible2('Y','N','N','N','N','Y','N');
            }

            if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                fnPopButtonVisible2('N','N','N','N','N','N','N');
            }
            /********************** 공통버튼 및 IME 처리 종료 *********************/

            //해더클릭 설정
            //$("#step02").attr("onclick", 'step(\'<c:url value="/abg/abgpDvrsAplyUserIf02/index.do" />\', \'abgpDvrsAplyUserIf02\', setNextValue)');
            $("#step03").attr("onclick", 'step(\'<c:url value="/abg/abgpDvrsAplyUserIf04/index.do" />\', \'abgpDvrsAplyUserIf04\', setNextValue)');
        });

        /********************** Javascript 변수 및 함수 설정 시작 *********************/

        function setNextValue(args){
            var rowId = gridObj1.getDataIDs();
            var commonMenuId = args[1];
            var result = true;

            //집행잔액이 아예 없거나 신규항목이있으면
            if(rowId.length == 0){
                result = false;
            }else{
                for(var i=0; i<rowId.length; i++){
                    //처음생성될때 수정모드가 켜있어서 이렇게 불러옴
                    var status1 = $("#"+rowId[i]+"_STATUS");
                    //값을 바꾸거나 조회후 수정모드 꺼져있어 이렇게 불러옴
                    var status2 = gridObj1.getCell(rowId[i], "STATUS");

                    if(status1.val() == undefined){
                        if(status2 == "C" && commonMenuId == 'abgpDvrsAplyUserIf04'){
                            result = false;
                        }
                    }else if(status1.val() == "C" && commonMenuId == 'abgpDvrsAplyUserIf04'){

                        result = false;
                    }
                }
            }
            //수정불가시 어차피 작성을 못하므로 화면넘김.
            if(result || "${dto.editable}" == 'false'){
                //스텝 후처리 함수(화면넘김)
                stepSucces(args);
            }else{
                alert("전용감액 저장 후, 전용증액을 작성할 수 있습니다.");
            }
        }

        function getbutton(cellvalue, options, rowObject){
            var btns = "";
            var rowId = options.rowId;

            //if(rowObject.STATUS == "C"){
            if("${dto.editable}" != 'false'){
                btns = '<a href="javascript:popAbgpNewExecBudg(\''+rowId+'\');"><img src="<c:url value="/images/btn_pop.gif" />" border=0 /></a>';
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
            popAbgpNewExecBudg(rowId);
        }

        function popAbgpExecBudg(rowId){
            var s_url = '<c:url value="/abg/abgpExecBudg/popup.do" />';
            //var s_params = '?rowId='+rowId+'&gridObj=grid1';

            fnOpenPopupResize("search_popAbgpExecBudg", "about:blank", 1024, 500);
            $("#popupForm").find("#rowId").val(rowId);
            $("#popupForm").find("#gridObj").val("grid1");
            $("#popupForm").find("#gridObj").val("grid1");
            $("#popupForm").attr("target", "search_popAbgpExecBudg");

            $("#popupForm").attr("action", s_url);
            $("#popupForm").setAuthSync(); //권한 동기화
            $("#popupForm").submit();

            //fnOpenPopup("search_popAbgpExecBudg", s_url+s_params, 1300, 500);
        }

        var popupAbgpExecBudg;

        //신규 실행예산 팝업 사업담당자만 나옴
        function popAbgpNewExecBudg(rowId){
            var s_url = '<c:url value="/abg/abgpNewExecBudg/popup.do" />';
            var pyear = '${setYear}';
            var rnd_yn = '${commonProgramAuth.ctrlSetpVar1.RND}';

            popupAbgpExecBudg = fnOpenPopup("search_popAbgpExecBudg", "about:blank", 1024, 500);

            $("#popupForm").find("#rowId").val(rowId);
            $("#popupForm").find("#gridObj").val("grid1");
            if(rnd_yn == "Y") {
                $("#popupForm").find("#p_year").val(""); //15.01.05 연구전용 연도조건 해제.
                $("#popupForm").find("#searchBIZ_NO").val($('#searchBIZ_NO', parent.document).val()); //19.12 예산전용화면 개선 사업번호로 실행예산 검색
            } else {
                //$("#popupForm").find("#p_year").val(pyear);
                $("#popupForm").find("#p_year").val("");  // 18.01.02 행정 예산전용 연도조건 해제(예산팀 정진섭 요청).

                // 20180402 하효선 요청 : 예산전용에서 타부서의 예산 조회 가능.
                $("#popupForm").find("#searchNotPermissionDeptYn").val("Y");
            }



            $("#popupForm").find("#rnd_yn").val(rnd_yn);
            $("#popupForm").find("#searchNotPermissionDeptYn").val("Y");
            $("#popupForm").attr("target", "search_popAbgpExecBudg");
            $("#popupForm").attr("action", s_url);
            $("#popupForm").setAuthSync(); //권한 동기화
            $("#popupForm").submit();
        }

        // '실행예산' 팝업창에서 단일, 다중 선택처리  jytest
        function setArrExecBudg(gridObj, rowId ,selectedRowArr, multiChkYn) {
            popupAbgpExecBudg.close();

            // 2017.12.21 예산배정 체크
            if(selectedRow['ASGN_CNT'] > 0) {
                setTimeout(
                    function(){
                        var msgTxt = "* 예산배정신청 진행중인 예산은 전용 할 수 없습니다.\n 예산배정신청 메뉴에서 해당예산을 확인하십시오.";
                        // 연구전용 팝업일때
                        if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y"){
                            msgTxt += "\n\n※ '반려' 상태인 배정신청건은 재신청이 가능하므로 해당배정신청건 삭제이후 전용할 수 있습니다.";
                        }
                        alert(msgTxt);
                    }
                    ,500);
            } else {
                var id = gridObj1.getDataIDs();
                var selectedBUDG_DMD_NO = "";

                for(var i=0; i<id.length; i++) {
                    var budg_dmd_no = getGridCell(gridObj1, id[i], "BUDG_DMD_NO");

                    if(budg_dmd_no == selectedRow['BUDG_DMD_NO']) {
                        alert("이미 등록된 예산요구가 있습니다.");
                        return false;
                    }
                }

                if(getGridCell(gridObj1, rowId, "STATUS") != "C") {
                    setGridCell(gridObj1, rowId, "STATUS", "U");
                }

                setGridCell(gridObj1, rowId, "BUDG_DMD_NO", selectedRow['BUDG_DMD_NO']);
                setGridCell(gridObj1, rowId, "BIZ_MCD", selectedRow['BIZ_MCD']);
                setGridCell(gridObj1, rowId, "BIZ_MCD_NM", selectedRow['BIZ_MCD_NM']);
                setGridCell(gridObj1, rowId, "BIZ_SCD", selectedRow['BIZ_SCD']);
                setGridCell(gridObj1, rowId, "BIZ_SCD_NM", selectedRow['BIZ_SCD_NM']);
                setGridCell(gridObj1, rowId, "BIZ_CLCD_NM", selectedRow['BIZ_CLCD_NM']);
                setGridCell(gridObj1, rowId, "BIZ_SDCD_NM", selectedRow['BIZ_SDCD_NM']);
                setGridCell(gridObj1, rowId, "BIZ_NO", selectedRow['BIZ_NO']);
                setGridCell(gridObj1, rowId, "BIZ_NM", selectedRow['BIZ_NM']);
                setGridCell(gridObj1, rowId, "BIZ_DIV", selectedRow['BIZ_DIV']);
                setGridCell(gridObj1, rowId, "BIZ_LRG_DCD", selectedRow['BIZ_LRG_DCD']);
                setGridCell(gridObj1, rowId, "BUDG_SBJT_CD", selectedRow['BUDG_SBJT_CD']);
                setGridCell(gridObj1, rowId, "BUDG_SBJT_NM", selectedRow['BUDG_SBJT_NM']);
                setGridCell(gridObj1, rowId, "TT_AMT", selectedRow['TT_AMT']);
                setGridCell(gridObj1, rowId, "CURR_AMT", selectedRow['TT_AMT']);
                setGridCell(gridObj1, rowId, "ASGN_ACCP_AMT", selectedRow['ASGN_ACCP_AMT']); // 배정액
                setGridCell(gridObj1, rowId, "EXP_CAUS_AMT", selectedRow['EXP_CAUS_AMT']); // 원인금액
                // 지출금액 EXP_SUM_AMT
                setGridCell(gridObj1, rowId, "ACCT_CHAR_EMPNO", selectedRow['ACCT_CHAR_EMPNO']);
                setGridCell(gridObj1, rowId, "ACCT_CHAR_EMPNM", selectedRow['ACCT_CHAR_EMPNM']);
                //setGridCell(gridObj1, rowId, "BALANCE_AMT", selectedRow['AMT2']);
                setGridCell(gridObj1, rowId, "STUDT_LABCOST_YN", selectedRow['STUDT_LABCOST_YN']);

                if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y"){
                    setGridCell(gridObj1, rowId, "EXP_CAUS_AMT1", selectedRow['TT_AMT'] -  selectedRow['EXP_CAUS_AMT']);
                    setGridCell(gridObj1, rowId, "EXP_SUM_AMT1", selectedRow['TT_AMT'] -  selectedRow['EXP_SUM_AMT']);

                    if(selectedRow['EXP_SUM_AMT'] > selectedRow['EXP_CAUS_AMT']) {
                        setGridCell(gridObj1, rowId, "ASGN_ACCP_AMT1", selectedRow['ASGN_ACCP_AMT'] - selectedRow['EXP_SUM_AMT']);
                    } else {
                        setGridCell(gridObj1, rowId, "ASGN_ACCP_AMT1", selectedRow['ASGN_ACCP_AMT'] - selectedRow['EXP_CAUS_AMT']);
                    }
                } else {
                    setGridCell(gridObj1, rowId, "BALANCE_AMT", selectedRow['AMT2']);
                }

                var TtAmt = Number(selectedRow['TT_AMT']);
                var AsgnAccpAmt = Number(selectedRow['ASGN_ACCP_AMT']);
                var AsgnRate = (AsgnAccpAmt/TtAmt*100).toFixed(1).toString() + '%';
                setGridCell(gridObj1, rowId, "ASGN_RATE", AsgnRate);

                if(budg_dmd_no == "") {
                    budg_dmd_no = selectedRow['BUDG_DMD_NO'];
                }

                for(var i=0; i<id.length; i++) {
                    if(getGridCell(gridObj1, id[i], "BUDG_DMD_NO") != ""){
                        selectedBUDG_DMD_NO += budg_dmd_no + " ,";
                    }
                }

                $("#selectedBUDG_DMD_NO2").val(selectedBUDG_DMD_NO.substring(0, selectedBUDG_DMD_NO.length -1));

                fnSearch(gridObj2, null, null, '');
            }
        }

        function fnCheckAMT(e){
            var obj = $(e.target);
            var gridTbl = obj.closest('table.ui-jqgrid-btable');
            var row = obj.closest('tr.jqgrow');
            var id = row.attr('id');
            var BALANCE_AMT = Number(gridTbl.getCell(id, "BALANCE_AMT"));

            // cch
            var BALANCE_AMT = -1;
            if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y") {
                BALANCE_AMT = Number(gridTbl.getCell(id, "ASGN_ACCP_AMT1"));
            } else {
                BALANCE_AMT = Number(gridTbl.getCell(id, "BALANCE_AMT"));
            }

            var DVRS_AMT = Math.abs(Number(gridTbl.find('#'+id+'_DVRS_AMT').val().replace(/,/gi,"")));
            var BUDG_SBJT_CD = gridTbl.getCell(id, "BUDG_SBJT_CD");

            if(BUDG_SBJT_CD != ''){
                if(BALANCE_AMT < DVRS_AMT){
                    //얼럿창 뜰시 포커스아웃 이벤트가 실행되서 0원처리 먼저
                    gridTbl.find('#'+id+'_DVRS_AMT').val("0");
                    alert("전용 요구액은 잔액을 초과할 수 없습니다.");
                    return;
                }
            }else{
                //얼럿창 뜰시 포커스아웃 이벤트가 실행되서 0원처리 먼저
                gridTbl.find('#'+id+'_DVRS_AMT').val("0");
                alert("예산과목을 먼저 선택하여 주십시오.");
                return;
            }

            //합계금액 실시간반영
            if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y") {

                <!-- 2020.06.19 그리드 합계표기할 때 예산합계제외처리하여 표기(연구비전용대상에서 예외 예산) -->
                if(nullValue(gridTbl.getCell(id, "EXCEPT_SUM_BUDG_YN"), 'N') == 'N') {
                    var sum_dvrs_amt = (gridObj1.getCol('DVRS_AMT', false, 'sum'));

                    var array = gridObj1.getDataIDs();
                    $.each(array, function() {
                        var row = gridObj1.getRowData(this);

                        if(row.EXCEPT_SUM_BUDG_YN == 'Y') {
                            sum_dvrs_amt = sum_dvrs_amt - row.DVRS_AMT;
                        }
                    });

                    sum_dvrs_amt = -(Math.abs(sum_dvrs_amt) + Math.abs(DVRS_AMT));
                    gridObj1.jqGrid('footerData', 'set', {RNUM:'합계', DVRS_AMT:sum_dvrs_amt});
                }
            } else {
                var sum_dvrs_amt = (gridObj1.getCol('DVRS_AMT', false, 'sum'));
                sum_dvrs_amt = -(Math.abs(sum_dvrs_amt) + Math.abs(DVRS_AMT));
                gridObj1.jqGrid('footerData', 'set', {BALANCE_AMT:'합계', DVRS_AMT:sum_dvrs_amt});
            }
        }

        function fnMinusAMT(e){
            var obj = $(e.target);
            var gridTbl = obj.closest('table.ui-jqgrid-btable');
            var row = obj.closest('tr.jqgrow');
            var id = row.attr('id');
            var DVRS_AMT = Number(gridTbl.find('#'+id+'_DVRS_AMT').val().replace(/,/gi,""));
            //gridTbl.setCell(id, "DVRS_AMT", -Math.abs(DVRS_AMT)); 저장버튼 클릭시 FocusOut이벤트가 발생되어 save이벤트의 getgrid와 겹쳐서 사용하면안됨
            gridTbl.find('#'+id+'_DVRS_AMT').val(Math.abs(DVRS_AMT));
        }

        function fnSearchGrid2(){
            var id = gridObj1.getDataIDs();
            var selectedBUDG_DMD_NO = "";
            for(var i=0; i<id.length; i++){
                if(gridObj1.getCell(id[i], "BUDG_DMD_NO") !=  ""){
                    selectedBUDG_DMD_NO += gridObj1.getCell(id[i], "BUDG_DMD_NO")+",";
                }

            }

            $("#selectedBUDG_DMD_NO2").val(selectedBUDG_DMD_NO.substring(0, selectedBUDG_DMD_NO.length -1));
            fnSearch(gridObj2, null, null, '');
        }

        function fnComplate() {

            <!-- 2020.06.19 그리드 합계표기할 때 예산합계제외처리하여 표기(연구비전용대상에서 예외 예산) -->
            if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y") {

                var sum_dvrs_amt = gridObj1.getCol('DVRS_AMT', false, 'sum');

                var array = gridObj1.getDataIDs();
                $.each(array, function() {
                    var row = gridObj1.getRowData(this);

                    if(row.EXCEPT_SUM_BUDG_YN == 'Y') {
                        sum_dvrs_amt = sum_dvrs_amt - row.DVRS_AMT;
                    }
                });

                gridObj1.jqGrid('footerData', 'set', {RNUM:'합계', DVRS_AMT:sum_dvrs_amt});

            } else {
                var sum_dvrs_amt = (gridObj1.getCol('DVRS_AMT', false, 'sum'));
                gridObj1.jqGrid('footerData', 'set', {BALANCE_AMT:'합계', DVRS_AMT:sum_dvrs_amt});
            }
        }

        /************** 저장시 그리드 체크 시작 ***************/

        function fnRowCount() {
            if(gridObj1.getDataIDs().length == 0) {
                alert('감액정보를 \'신규\'입력 후 저장해주세요.');
                return false;
            } else return true;
        }

        function gridObj1RowCheck(){
            var id = gridObj1.getDataIDs();
            var selectedBUDG_DMD_NO = "";
            var over_dvrs_amt_list = '';

            for(var i=0; i<id.length; i++){
                if(gridObj1.getCell(id[i], "BUDG_DMD_NO") !=  "") {
                    selectedBUDG_DMD_NO += gridObj1.getCell(id[i], "BUDG_DMD_NO")+",";
                } else{
                    if('${dto.mode}' == 'ifrm') {
                        parent.unWrap();
                    } else {
                        wrapWindowByUnMask();
                    }

                    alert("예산요구를 등록해주세요.");
                    return;
                }
            }
            /*
            if(over_dvrs_amt_list != ''){
                alert('학생인건비(통합) 예산 5%이상 증액/감액은 전담기관(연구재단)의 승인이 필요합니다.\n\n' + over_dvrs_amt_list);
            }
            */

            // 증액정보와 중복된 예산이 있는지 확인
            $("#selectedBUDG_DMD_NO").val(selectedBUDG_DMD_NO.substring(0, selectedBUDG_DMD_NO.length -1));
            $("#searchForm").ajaxSubmit({
                dataType: 'json'
                , url: '<c:url value="/abg/abgpDvrsAplyUserIf02/selectDvrsPlusBudgDmd.do" />'
                , success: function (data) {
                    var array = data.list;
                    if (array != "" && $("#selectedBUDG_DMD_NO").val() != "") {
                        if('${dto.mode}' == 'ifrm') { //전용화면 개선
                            parent.unWrap();
                        } else {
                            wrapWindowByUnMask();
                        }

                        alert("전용증가와 중복된 예산요구입니다.");
                    } else {
                        checkDvrsBudg();
                    }
                }, error: function (data, status, err) {
                    if('${dto.mode}' == 'ifrm') { //전용화면 개선
                        parent.unWrap();
                    } else {
                        wrapWindowByUnMask();
                    }

                    alert(<spring:message code="AbgeDvrsAplyUser.alert.06" text= "'시스템 작업중입니다.[' + data.status + ']'" />);
                }
            });
        }

        // 전용중인 예산요구가 있는지 확인
        function checkDvrsBudg() {
            $("#searchForm").ajaxSubmit({
                dataType: 'json'
                , url: '<c:url value="/abg/abgpDvrsAplyUserIf02/selectDvrsBudgDmd.do" />'
                , success: function (data) {
                    var array = data.list;
                    var chkCnt = 0;
                    var dvrsBudgDmdNo = "";

                    $.each(array, function () {
                        if (this.CNT > 0) {
                            dvrsBudgDmdNo = dvrsBudgDmdNo + this.BUDG_DMD_NO + ", ";
                            chkCnt++;
                        }
                    });

                    if (chkCnt > 0) {
                        if('${dto.mode}' == 'ifrm') { //전용화면 개선
                            parent.unWrap();
                        } else {
                            wrapWindowByUnMask();
                        }
                        alert("실행예산(" + dvrsBudgDmdNo.substring(0, dvrsBudgDmdNo.length - 2) + ")이 전용승인전입니다. 승인이후 선택해주십시오.");
                    } else {
                        checkBizDiv();
                    }

                }, error: function (data, status, err) {
                    if('${dto.mode}' == 'ifrm') { //전용화면 개선
                        parent.unWrap();
                    } else {
                        wrapWindowByUnMask();
                    }
                    alert(<spring:message code="AbgeDvrsAplyUser.alert.06" text= "'시스템 작업중입니다.[' + data.status + ']'" />);
                }
            });
        }

        function checkBizDiv(){
            $("#searchForm").ajaxSubmit({
                dataType: 'json'
                , url: '<c:url value="/abg/abgpDvrsAplyUserIf02/selectBizInfo.do" />'
                , success: function(data) {
                    var id = gridObj1.getDataIDs();

                    console.log('id : ' + id);

                    // 2020.06.10 감액정보가 등록되어 있으면 체크진행
                    if(id != '') {
                        var result = true;

                        var bizDiv		= getGridValue(gridObj1, id[0], "BIZ_DIV");
                        var bizLrgDcd	= getGridValue(gridObj1, id[0], "BIZ_LRG_DCD");

                        //그리드데이터 내 체크
                        for (var i = 0; i < id.length; i++) {
                            var cmpBizDiv = getGridValue(gridObj1, id[i], "BIZ_DIV");
                            var cmpBizLrgDcd = getGridValue(gridObj1, id[i], "BIZ_LRG_DCD");
                            var budgDmdNo = getGridValue(gridObj1, id[i], "BUDG_DMD_NO");

                            //alert("bizDiv:"+bizDiv+", bizLrgDcd:"+bizLrgDcd+", cmpBizDiv:"+cmpBizDiv+", cmpBizLrgDcd:"+cmpBizLrgDcd);

                            if ((bizDiv != '' && bizLrgDcd != '') || (cmpBizDiv != '' && cmpBizLrgDcd != '')) {
                                if (cmpBizDiv != bizDiv || cmpBizLrgDcd != bizLrgDcd) {
                                    if('${dto.mode}' == 'ifrm') { //전용화면 개선
                                        parent.unWrap();
                                    } else {
                                        wrapWindowByUnMask();
                                    }
                                    alert("예산(" + budgDmdNo + ")의 사업구분과 대분류가 일치하지않습니다.");
                                    result = false;
                                    return;
                                }
                            } else {
                                if('${dto.mode}' == 'ifrm') { //전용화면 개선
                                    parent.unWrap();
                                } else {
                                    wrapWindowByUnMask();
                                }
                                alert("예산을 입력해주십시오.");
                                result = false;
                                return;
                            }
                        }

                        //연구실행예산등록시 하나의 사업만 신청 가능
                        var bizNo = gridObj1.getCell(id[0], "BIZ_NO");
                        if ((bizDiv != 'ABG18.20' || bizLrgDcd != 'ABG02.2')) {
                            for (var x = 1; x < id.length; x++) {
                                var cmpBizNo = gridObj1.getCell(id[x], "BIZ_NO");
                                if (bizNo != cmpBizNo) {
                                    if('${dto.mode}' == 'ifrm') { //전용화면 개선
                                        parent.unWrap();
                                    } else {
                                        wrapWindowByUnMask();
                                    }
                                    alert("하나의 사업의 예산만 등록해주십시오.");
                                    result = false;
                                    return;
                                }
                            }
                        }

                        //alert(data.BIZ_DIV+", "+bizDiv+", "+data.BIZ_LRG_DCD+", "+bizLrgDcd);

                        //json 데이터 체크
                        if (data.BIZ_DIV != "") {
                            if (data.BIZ_DIV != bizDiv || data.BIZ_LRG_DCD != bizLrgDcd) {
                                if('${dto.mode}' == 'ifrm') { //전용화면 개선
                                    parent.unWrap();
                                } else {
                                    wrapWindowByUnMask();
                                }

                                alert("사업구분: " + data.BIZ_DIV_NM + ", 대분류: " + data.BIZ_LRG_NM + "인 사업의 예산을 등록해야합니다.");
                                return false;
                            }
                            //일반행정 전용은 전용사업정보를 미리 넣어서 비엇으면 연구인데 비수탁 비R&D면 말이안됨
                        } else if (bizDiv == 'ABG18.20' && bizLrgDcd == 'ABG02.2') {
                            if('${dto.mode}' == 'ifrm') { //전용화면 개선
                                parent.unWrap();
                            } else {
                                wrapWindowByUnMask();
                            }
                            alert("사업구분: 비수탁(출연금등), 대분류: 비R&D 인 사업의 예산은 일반행정에 등록해야합니다.");
                            return false;
                        }

                        //if(result) budgDmdCheck();

                        /* 2019.09.27 증액사업과 같은지 체크 */
                        if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y") {

                            var url = '<c:url value="/abg/abgpDvrsAplyUserIf02/selectPlusBizNo.do" />';
                            fnAjaxSubmit(url, '#searchForm', function(minusData) {
                                if(minusData != null) {
                                    // 증액내역에 등록한 사업과 감액내역에 등록한 사업을 비교
                                    // 수탁은 동일사업 내 목간전용만 가능
                                    // 기본사업은 타사업간전용 가능(안내메세지는 뛰운다.)
                                    if(bizNo != nullValue(minusData.PLUS_BIZ_NO, '')) {
                                        // 수탁은 타사업간전용은 허용하지 않는다.
                                        if(minusData.PLUS_BIZ_DIV == 'ABG18.10') {
                                            if('${dto.mode}' == 'ifrm') { //전용화면 개선
                                                parent.unWrap();
                                            } else {
                                                wrapWindowByUnMask();
                                            }
                                            alert('증액내역의 사업과 다릅니다.\n연구비예산 전용은 감액/증액 사업이 일치하여야 합니다.');
                                            return;
                                        }
                                        // 기본사업은 타사업간전용을 허용하되 alert를 띄운다.
                                        else if(minusData.PLUS_BIZ_DIV == 'ABG18.20') {
                                            if(confirm('타사업간 전용인데 계속 진행하시겠습니까?')) {
                                                if(result) budgDmdCheck();
                                            } else {
                                                if('${dto.mode}' == 'ifrm') { //전용화면 개선
                                                    parent.unWrap();
                                                } else {
                                                    wrapWindowByUnMask();
                                                }
                                                return;
                                            }
                                        }
                                    } else {
                                        // 등록된 증액내역의 사업과 같은 케이스
                                        if(result) budgDmdCheck();
                                    }
                                } else {
                                    // 증액내역이 등록되지 않았는 케이스
                                    if(result) budgDmdCheck();
                                }
                            }, null, true);
                        } else {
                            budgDmdCheck();
                        }
                    } else {
                        if('${dto.mode}' == 'ifrm') { //전용화면 개선
                            parent.unWrap();
                        } else {
                            wrapWindowByUnMask();
                        }
                    }

                }, error: function(data, status, err) {
                    if('${dto.mode}' == 'ifrm') { //전용화면 개선
                        parent.unWrap();
                    } else {
                        wrapWindowByUnMask();
                    }
                    alert(<spring:message code="AbgeDvrsAplyUser.alert.06" text= "'시스템 작업중입니다.[' + data.status + ']'" />);
                }
            });
        }

        function budgDmdCheck(){
            var rows = gridObj1.getDataIDs();
            var biz_no = "0";
            var tmp_biz_no = "0";
            var BUDG_SBJT_CD = "0";
            var tmp_BUDG_SBJT_CD = "0";

            var biz_scd = "";
            var tmp_biz_scd = "";
            var chkFlag = true;

            var plus_DVRS_AMT = 0;
            var plus_BALANCE_AMT = 0;
            $("#MINUS_DVRS_AMT").val("0");
            $("#DVRS_BALANCE_AMT").val("0");

            var rnd_yn = '${commonProgramAuth.ctrlSetpVar1.RND}';

            if(rows.length > 0){
                $("#searchForm").ajaxSubmit({
                    dataType: 'json'
                    , url: '<c:url value="/abg/abgpDvrsAplyUserIf02/selectDmdInfo.do" />'
                    , success: function(data){
                        var array = data.list;
                        $("#gridObj1BIZ_NO").val(biz_no);
                        $("#gridObj1BUDG_SBJT_CD").val(BUDG_SBJT_CD);

                        if(rnd_yn == "Y"){
                            if(array != "" && $("#selectedBUDG_DMD_NO").val() != ""){
                                for(var i=0; i<array.length; i++){
                                    biz_no = array[i].UP_BIZ_NO;
                                    BUDG_SBJT_CD = array[i].BUDG_SBJT_CD;
                                    if(tmp_biz_no != biz_no && tmp_biz_no != "0"){

                                        if(tmp_BUDG_SBJT_CD != BUDG_SBJT_CD && tmp_BUDG_SBJT_CD != "0"){
                                            if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                                                parent.unWrap();
                                            } else {
                                                wrapWindowByUnMask();
                                            }

                                            alert("상위사업이 같거나 예산과목이 같은 예산을 선택해 주십시오.");
                                            $("#gridObj1BIZ_NO").val("");
                                            $("#gridObj1BUDG_SBJT_CD").val("");
                                            chkFlag = false;
                                            break;
                                        }else{
                                            $("#gridObj1BIZ_NO").val("");
                                            $("#gridObj1BUDG_SBJT_CD").val(BUDG_SBJT_CD);
                                        }
                                    }else{
                                        $("#gridObj1BIZ_NO").val(biz_no);
                                        if(tmp_BUDG_SBJT_CD != BUDG_SBJT_CD && tmp_BUDG_SBJT_CD != "0"){
                                            $("#gridObj1BUDG_SBJT_CD").val("");
                                        }else{
                                            $("#gridObj1BUDG_SBJT_CD").val(BUDG_SBJT_CD);
                                        }
                                    }
                                    tmp_BUDG_SBJT_CD = BUDG_SBJT_CD;
                                    tmp_biz_no = biz_no;
                                }
                            }
                        }else if(rnd_yn == "N"){
                            /* 2018.03.09 예산전용 프로세스 개편
                                일반행정 예산전용은 동일한 소분류 사업만 등록 가능.*/
                            if(array != "" && $("#selectedBUDG_DMD_NO").val() != ""){
                                for(var i=0; i<array.length; i++){
                                    biz_no = array[i].UP_BIZ_NO;
                                    BUDG_SBJT_CD = array[i].BUDG_SBJT_CD;
                                    biz_scd = array[i].BIZ_SCD;

                                    if(tmp_biz_scd != biz_scd && tmp_biz_scd != ""){
                                        if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                                            parent.unWrap();
                                        } else {
                                            wrapWindowByUnMask();
                                        }

                                        alert("동일한 소분류 사업만 등록 가능합니다.");
                                        $("#gridObj1BIZ_NO").val("");
                                        $("#gridObj1BUDG_SBJT_CD").val("");
                                        $("#gridObj1BIZ_SCD").val("");
                                        chkFlag = false;
                                        break;
                                    }else{
                                        $("#gridObj1BIZ_NO").val(biz_no);
                                        $("#gridObj1BUDG_SBJT_CD").val(BUDG_SBJT_CD);
                                        $("#gridObj1BIZ_SCD").val(biz_scd);
                                    }
                                    tmp_biz_scd = biz_scd;
                                }
                            }
                        }

                        if(chkFlag){
                            for(var i=0; i<rows.length; i++){
                                var status = gridObj1.find("#"+rows[i]+"_STATUS");
                                if(status.val() == '' || status.val() == undefined){
                                    //var DVRS_AMT = gridObj1.getCell(rows[i], "DVRS_AMT");
                                    var DVRS_AMT = getCellValue(gridObj1, rows[i], "DVRS_AMT");
                                    //var BALANCE_AMT = gridObj1.getCell(rows[i], "BALANCE_AMT");
                                    var BALANCE_AMT = -1;
                                    if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y") {
                                        BALANCE_AMT = gridObj1.getCell(rows[i], "ASGN_ACCP_AMT1");
                                    } else {
                                        BALANCE_AMT = gridObj1.getCell(rows[i], "BALANCE_AMT");
                                    }

                                    plus_DVRS_AMT = plus_DVRS_AMT + Number(DVRS_AMT.replace(/,/gi,""));
                                    plus_BALANCE_AMT = plus_BALANCE_AMT + Number(BALANCE_AMT.replace(/,/gi,""));
                                    //gridObj1.setCell(rows[i], "STATUS", "U");
                                }else if(status.val() == "C" || status.val() == "U"){
                                    var DVRS_AMT = gridObj1.find("#"+rows[i]+"_DVRS_AMT").val().replace(/,/gi,"");
                                    //var BALANCE_AMT = gridObj1.getCell(rows[i], "BALANCE_AMT");
                                    var BALANCE_AMT = -1;
                                    if("${commonProgramAuth.ctrlSetpVar1.RND}" == "Y") {
                                        BALANCE_AMT = gridObj1.getCell(rows[i], "ASGN_ACCP_AMT1");
                                    } else {
                                        BALANCE_AMT = gridObj1.getCell(rows[i], "BALANCE_AMT");
                                    }

                                    plus_DVRS_AMT = plus_DVRS_AMT + Number(DVRS_AMT.replace(/,/gi,""));
                                    plus_BALANCE_AMT = plus_BALANCE_AMT + Number(BALANCE_AMT.replace(/,/gi,""));
                                    //alert("plus_DVRS_AMT :: " + plus_DVRS_AMT + "\n\nplus_BALANCE_AMT :: " + plus_BALANCE_AMT);
                                    if(status.val() == "C" || status.val() == "D"){

                                    }else{
                                        //status.val("U");
                                    }
                                }

                            }

                            $("#MINUS_DVRS_AMT").val(plus_DVRS_AMT);
                            $("#DVRS_BALANCE_AMT").val(plus_BALANCE_AMT);


                            totCheck();
                        }

                    }, error: function(data, status, err) {
                        if('${dto.mode}' == 'ifrm') { //전용화면 개선
                            parent.unWrap();
                        } else {
                            wrapWindowByUnMask();
                        }
                        alert(<spring:message code="AbgeDvrsAplyUser.alert.06" text= "'시스템 작업중입니다.[' + data.status + ']'" />);
                    }
                });


            }else{
                parent.unWrap();
                alert("전용감소 항목을 입력 하여 주십시오.");
            }
        }

        function totCheck(){
            $("#searchForm").ajaxSubmit({
                dataType: 'json'
                , url: '<c:url value="/abg/abgpDvrsAplyUserIf02/selectDvrsPlusAtmSum.do" />'
                , success: function(data){
                    var array = data.list;
                    var result = false;

                    var MINUS_DVRS_AMT = $("#MINUS_DVRS_AMT").val().replace("-","");
                    var DVRS_BALANCE_AMT = $("#DVRS_BALANCE_AMT").val();

                    if(array != ""){
                        var gridObj1BIZ_NO = $("#gridObj1BIZ_NO").val();
                        var compareBIZ_NO = array[0].UP_BIZ_NO;
                        var gridObj1BUDG_SBJT_CD = $("#gridObj1BUDG_SBJT_CD").val();
                        var compareBUDG_SBJT_CD = array[0].BUDG_SBJT_CD;
                        var PLUS_DVRS_AMT = array[0].PLUS_DVRS_AMT;

                        var gridObj1BIZ_SCD = $("#gridObj1BIZ_SCD").val();
                        var compareBIZ_SCD = array[0].BIZ_SCD;

                        var rnd_yn = '${commonProgramAuth.ctrlSetpVar1.RND}';

                        for(var i=0; i<array.length; i++){
                            if(array[0].UP_BIZ_NO != array[i].UP_BIZ_NO) compareBIZ_NO = "";
                            if(array[0].BUDG_SBJT_CD != array[i].BUDG_SBJT_CD) compareBUDG_SBJT_CD = "";
                            if(array[0].BIZ_SCD != array[i].BIZ_SCD) compareBIZ_SCD = "";
                        }

                        if(rnd_yn == "Y"){
                            if(gridObj1BIZ_NO == compareBIZ_NO){
                                result = true;
                            }else{
                                if(gridObj1BUDG_SBJT_CD == compareBUDG_SBJT_CD){
                                    result = true;
                                }else{
                                    alert("상위사업이 같거나 예산과목이 같은 예산만 전용 가능합니다.");
                                }
                            }
                        }else if(rnd_yn == "N"){
                            /* 2018.03.09 예산전용 프로세스 개편
                            일반행정 예산전용은 동일한 소분류 사업만 등록 가능.*/

                            if(gridObj1BIZ_SCD == compareBIZ_SCD){
                                result = true;
                            }else{
                                alert("증액예산과 동일한 소분류 사업만 등록 가능합니다.");
                            }

                            /*
                            * 비RND 사업간 전용 시 예산기간관리에서 "전용가능기간"에 지정된 기간 내에서만 가능
                            * 2020.12.24 조희태
                            * */
                            var gridObj1DataIDs = gridObj1.getDataIDs();

                            // 감액 & 증액 데이타가 있을때만 체크
                            if(gridObj1DataIDs.length > 0 && array.length > 0) {
                                var isBizNoMisMatch = false;

                                // 감액 & 증액 간 세부사업번호가 다른게 있는지 체크
                                for (var i=0; i<gridObj1DataIDs.length; i++) {
                                    for (var j=0; j<array.length; j++) {
                                        if(getGridValue(gridObj1, gridObj1DataIDs[i], "BIZ_NO") != array[j].BIZ_NO) {
                                            isBizNoMisMatch = true;
                                        }
                                    }
                                }

                                // 감액 & 증액 간 세부사업번호가 일치하지 않는다면 * 예산기간관리에서 "전용가능기간"에 지정된 기간
                                if(isBizNoMisMatch && data.str == "0") {
                                    alert("타사업간전용 신청기간이 아닙니다");
                                    result = false;
                                }
                            }
                        }
                        //결과가 없을때
                    }else{
                        if($("#gridObj1BIZ_NO").val() != "" || $("#gridObj1BUDG_SBJT_CD").val() != ""){
                            result = true;
                        }
                    }

                    if(result){

                        if(MINUS_DVRS_AMT == "0"){
                            if('${dto.mode}' == 'ifrm') { //전용화면 개선
                                parent.unWrap();
                            } else {
                                wrapWindowByUnMask();
                            }
                            alert("전용감소 금액은 0원 이상이여야 합니다.");
                            return;
                        } else if(Number(DVRS_BALANCE_AMT) < Number(MINUS_DVRS_AMT)){
                            if('${dto.mode}' == 'ifrm') { //전용화면 개선
                                parent.unWrap();
                            } else {
                                wrapWindowByUnMask();
                            }
                            alert("전용 요구액은 잔액을 초과할 수 없습니다.");
                            return;
                        }

                        var rowId = gridObj1.getDataIDs();
                        var isGridChanged = false;

                        for(var i = 0; i < rowId.length; i++){
                            var status = getCellValue(gridObj1, rowId[i], "STATUS");

                            if(status != '' && status != undefined){
                                isGridChanged = true;
                            }

                            if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                                if(status == ''){
                                    //gridObj1.setCell(rowId[i], "STATUS", 'U');
                                }
                                gridObj1.setCell(rowId[i], "DVRS_NO", $("#searchDVRS_NO").val());
                            }
                        }

                        if(!isGridChanged) {
                            if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                                parent.unWrap();
                                saveCallBack();
                            } else {
                                wrapWindowByUnMask();
                                alert('저장할 내용이 없습니다.');
                                return;
                            }
                        } else {
                            fnSave(gridObj1, saveUrl1, null, null, saveCallBack, '', null, null, function() {
                                if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                                    parent.unWrap();
                                } else {
                                    wrapWindowByUnMask();
                                }
                            });
                        }
                    }else{
                        if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                            parent.unWrap();
                        } else {
                            wrapWindowByUnMask();
                        }
                    }

                }, error: function(data, status, err) {
                    if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                        parent.unWrap();
                    } else {
                        wrapWindowByUnMask();
                    }
                    alert(<spring:message code="AbgeDvrsAplyUser.alert.06" text= "'시스템 작업중입니다.[' + data.status + ']'" />);
                }
            });
        }

        function saveCallBack(data){
            if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                parent.unWrap();
            } else {
                wrapWindowByUnMask();
            }

            if('${dto.mode}' == 'ifrm') { //전용 화면 개선
                parent.save('2', $("#searchDVRS_NO").val());
            } else {
                opener.search();
            }
        }

        function getCellValue(grid, id, fieldName){
            var value = grid.getCell(id, fieldName);

            if($('#' + grid.attr('id') + ' #' + id + '_' + fieldName).val() != undefined){
                value = $('#' + grid.attr('id') + ' #' + id + '_' + fieldName).val();
            }

            return value;
        }
        /************** 저장시 그리드 체크 종료 ***************/


        /*********************** Javascript 변수 및 함수 설정 종료 ***********************/
    </script>
</head>

<body>

<!-- 팝업 폼 설정 시작-->
<form id="popupForm" name="popupForm" method="post">
    <input type="hidden" id="rowId" name="rowId" value="" />
    <input type="hidden" id="gridObj" name="gridObj" value="" />
    <input type="hidden" id="p_year" name="p_year" value="" />
    <input type="hidden" id="rnd_yn" name="rnd_yn" value="" />

    <!-- 20180402 하효선 요청 : 예산전용에서 타부서의 예산 조회 가능. -->
    <input type="hidden" id="searchNotPermissionDeptYn" name="searchNotPermissionDeptYn" />

    <!-- 예산전용화면 개선 -->
    <input type="hidden" id="searchBIZ_NO" name="searchBIZ_NO" value="" class="ctl_input" />

    <!-- 다중선택 단일선택 (Y/N) -->
    <input type="hidden" id="searchMultiChkYn" name="searchMultiChkYn" value="" />
    </table>
</form>
<!-- 팝업 폼 설정 종료-->

<!-- 팝업 타이틀 및 버튼 시작 -->
<c:if test="${dto.mode ne 'ifrm'}" > <!-- 연구비 전용 화면 개선 -->
    <c:import url="/jsp/include/popbutton2.jsp" charEncoding="utf-8">
        <c:param name="paramPopupTitle" value="감액정보" />
    </c:import>
</c:if>
<!-- 팝업 타이틀 및 버튼 종료 -->

<!-- 데이터 조회조건 폼 시작 -->
<form id="searchForm" name="searchForm" method="post">
    <!-- 조회조건 공통항목 시작  -->
    <jsp:directive.include file="/jsp/include/comsearchelement.jsp"/>
    <!-- 조회조건 공통항목 종료  -->
    <input type="hidden" id="searchRND_YN" name="searchRND_YN" value="${commonProgramAuth.ctrlSetpVar1.RND}" />
    <input type="hidden" id="searchDVRS_NO" name="searchDVRS_NO" value="${dto.dvrs_no}" />
    <input type="hidden" id="selectedBUDG_DMD_NO" name="selectedBUDG_DMD_NO" />
</form>
<form id="searchForm1" name="searchForm1" method="post">
    <!-- 조회조건 공통항목 시작  -->
    <jsp:directive.include file="/jsp/include/comsearchelement.jsp"/>
    <!-- 조회조건 공통항목 종료  -->
    <input type="hidden" id="selectedBUDG_DMD_NO2" name="selectedBUDG_DMD_NO2" />
</form>

<form id="checkForm" name="checkForm" method="post">
    <input type="hidden" id="PLUS_DVRS_AMT" value="0" />
    <input type="hidden" id="MINUS_DVRS_AMT" value="0" />
    <input type="hidden" id="DVRS_BALANCE_AMT" value="0" />
    <input type="hidden" id="gridObj1BIZ_NO"/>
    <input type="hidden" id="gridObj1BUDG_SBJT_CD"/>
    <input type="hidden" id="gridObj1BIZ_SCD"/>
</form>

<!-- ********************** 일반 버튼 및 그리드 정의 시작 ********************** -->
<table border="0" cellpadding="0" cellspacing="0" style="width:100%;">
    <tr>
        <td valign="top">
            <div id="jqgrid1">
                <div class="grid_tbl_title">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td width="30%">감액정보</td>
                            <c:if test="${commonProgramAuth.ctrlSetpVar1.RND == 'Y'}">
                                <td align="left">
                                    <font color="white" >
									<span style="font-size:10pt;" >
										* 감액하려는 금액보다 전용가액(배정잔액)이 작은 경우 연구지원팀으로 문의 바랍니다.
									</span>
                                    </font>
                                </td>
                            </c:if>
                            <td align="right">
                                <c:if test="${dto.editable != 'false'}">
                                    <button id="btnMulti" title="다중선택" type="submit" tabindex="1"><span>다중선택</span></button>
                                    <button id="btnNew1" class="btn_new" title="신규" type="submit"><span class="blind">신규</span></button>
                                    <button id="btnDel1" class="btn_del" title="삭제" type="submit"><span class="blind">삭제</span></button>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </div>
                <table id="grid1" class="scroll"></table> <!-- 테이블 영역 -->
                <div id="pager1"></div> <!-- 페이징 영역 -->
            </div>
        </td>
    </tr>
    <tr>
        <td valign="top">
            <div id="jqgrid2">
                <div class="grid_tbl_title">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td width="30%">기지출 집행액</td>
                            <td align="right">
                            </td>
                        </tr>
                    </table>
                </div>
                <table id="grid2" class="scroll"></table> <!-- 테이블 영역 -->
                <div id="pager2"></div> <!-- 페이징 영역 -->
            </div>
        </td>
    </tr>
</table>
<!-- ********************** 일반 버튼 및 그리드 정의 종료 ********************** -->
<c:if test="${dto.mode ne 'ifrm'}" > <!-- 연구비 전용 화면 개선 -->
    <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td align="center">
                <input type="button" id="btnRtn" name="btnRtn" class="btn_bg2" title="이전" value="이전" />
                <input type="button" id="btnNext" name="btnNext" class="btn_bg2" title="다음" value="다음" />
            </td>
        </tr>
    </table>
</c:if>

<!-- 그리드 CUD 폼 시작 -->
<jsp:directive.include file="/jsp/include/comgridform.jsp"/>
<!-- 그리드 CUD 폼 종료 -->

</body>
</html>