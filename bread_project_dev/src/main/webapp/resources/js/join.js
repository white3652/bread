$(document).ready(function() {
	var isIdValid = false;
    var isValidPw = false;
    var isPw2 = false;
    var isValidName = false;
    var isValidPhoneNumber = false;
    var isValidNickname = false;

    // 에러 표시 함수
    function showError(inputId, message) {
        var inputField = $('#' + inputId);
        var errorSpan = inputField.next('.error-message');

        if (!errorSpan.length) {
            errorSpan = $('<span class="error-message"></span>').insertAfter(inputField);
        }

        if (message) {
            errorSpan.text(message).css('color', 'red').show();
            inputField.addClass('error-input');
        } else {
            errorSpan.hide();
            inputField.removeClass('error-input');
        }
    }

	$('#member_id').focusout(function() {
	    var idRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,15}$/;
	    if (!idRegex.test(this.value)) {
	        showError('member_id', '6~15자의 영문과 숫자로 구성해주세요.');
	        isIdValid = false;
	    } else if($("#member_id").val() != null) {
	        var member_id =  $("#member_id").val(); 
        	
	        $.ajax({
	            type : 'POST',
	            data : {"member_id": member_id},
	            url : "checkIdProcess.do",
	            success : function(resData) {
	                if (resData == "1") {
	                  showError('member_id', '이미 사용중인 아이디입니다.');
	                  isIdValid = false;
	                }else {
				        showError('member_id', '');
				        isIdValid = true;
				    }
	            },
	            error : function(error) {
	                
	                console.log("아이디 확인시 에러 발생");
	            }
	        });
	    }
	});
	
    $('#member_pw').focusout(function() {
    	var pwRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,15}$/;
        if (!pwRegex.test(this.value)) {
            showError('member_pw', '8~15자의 영문과 숫자로 구성해주세요.');
            isValidPw = false;
        } else {
            showError('member_pw', ''); // 에러 메시지 제거
            isValidPw = true;
        }
    });

    // 비밀번호 확인 검사
    $('#member_pw2').focusout(function() {
        if (this.value === "") {
            showError('member_pw2', '비밀번호 확인을 입력해주세요.');
            isPw2 = false;
        } else if (this.value !== $('#member_pw').val()) {
            showError('member_pw2', '비밀번호가 일치하지 않습니다.');
            isPw2 = false;
        } else {
            showError('member_pw2', ''); // 에러 메시지 제거
            isPw2 = true;
        }
    });


    $('#member_name').focusout(function() {
        var nameRegex = /^[가-힣]{2,10}$/;
        if (!nameRegex.test(this.value)) {
            showError('member_name', '본인의 이름을 정확히 입력해주세요.');
            isValidName = false;
        } else {
            showError('member_name', ''); // 에러 메시지 제거
            isValidName = true;
        }
    });

    $('#member_phone').focusout(function() {
        var phoneValue = this.value.replace(/[^0-9]/g, '');
        if (phoneValue.length > 11) {
            phoneValue = phoneValue.substring(0, 11);
        }
        this.value = phoneValue.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');

        if (this.value.replace(/-/g, '').length !== 11) {
            showError('member_phone', '11자리의 숫자를 정확히 입력해주세요.');
            isValidPhoneNumber = false;
        } else if($("#member_phone").val() != null) {
            var member_phone =  $("#member_phone").val(); 
        	
	        $.ajax({
	            type : 'POST',
	            data : {"member_phone": member_phone},
	            url : "checkPhoneNumberProcess.do",
	            success : function(resData) {
	                if (resData == "1") {
	                  showError('member_phone', '이미 등록된 전화번호입니다.');
	                  isValidPhoneNumber = false;
	                }else {
				        showError('member_phone', '');
				        isValidPhoneNumber = true;
				    }
	            },
	            error : function(error) {
	                
	                console.log("닉네임 확인시 에러 발생");
	            }
	        });
        } else {
            showError('member_phone', ''); // 에러 메시지 제거
            isValidPhoneNumber = true;
        }
    });

    $('#member_nickname').focusout(function() {
        if (this.value.length < 3 || this.value.length > 10) {
            showError('member_nickname', '3~10글자 사이로 입력해주세요.');
            isValidNickname = false;
        } else if($("#member_nickname").val() != null) {
            var member_nickname =  $("#member_nickname").val(); 
        	
	        $.ajax({
	            type : 'POST',
	            data : {"member_nickname": member_nickname},
	            url : "checkNicknameProcess.do",
	            success : function(resData) {
	                if (resData == "1") {
	                  showError('member_nickname', '이미 사용중인 닉네임입니다.');
	                  isValidNickname = false;
	                }else {
				        showError('member_nickname', '');
				        isValidNickname = true;
				    }
	            },
	            error : function(error) {
	                
	                console.log("닉네임 확인시 에러 발생");
	            }
	        });
        }
    });

    // 체크박스 로직
    $('#checkbox1').change(function() {
        $('.checkbox').prop('checked', $(this).is(':checked'));
    });

    $('.checkbox').not('#checkbox1').change(function() {
        $('#checkbox1').prop('checked', $('.checkbox').not('#checkbox1').length === $('.checkbox').not('#checkbox1:checked').length);
    });

    // 폼 제출 로직
    $('form[name="frm-join"]').submit(function(e) {
         var errorMessage = '';
        if (!isIdValid) {
            errorMessage += '아이디가 유효하지 않습니다.\n';
        }
        else if (!isValidPw) {
            errorMessage += '비밀번호가 유효하지 않습니다.\n';
        }
        else if (!isPw2) {
            errorMessage += '비밀번호 확인이 일치하지 않습니다.\n';
        }
        else if (!isValidName) {
            errorMessage += '이름이 유효하지 않습니다.\n';
        }
        else if (!isValidPhoneNumber) {
            errorMessage += '전화번호가 유효하지 않습니다.\n';
        }
        else if (!isValidNickname) {
            errorMessage += '닉네임이 유효하지 않습니다.\n';
        }
        else if (!$('#checkbox2').is(':checked')) {
            errorMessage += '이용약관 동의는 필수입니다.\n';
        }
        else if ($('#checkbox3').is(':checked')) {
            document.getElementById("option_agree").value = 1;
        }

        if (errorMessage !== '') {
            e.preventDefault(); // 폼 제출 중단
            alert(errorMessage); // 에러 메시지 표시
            return;
        }
        // 유효성 검사 통과 후 AJAX 요청 실행
        var formData = {
            member_id: $('#member_id').val(),
            member_pw: $('#member_pw').val(),
            member_name: $('#member_name').val(),
            member_phone: $('#member_phone').val(),
            member_nickname: $('#member_nickname').val(),
            option_agree: $('#checkbox3').is(':checked') ? 1 : 0
        };

		$.ajax({
		    type: 'POST',
		    url: 'joinProcess.do',
		    data: formData,
		    success: function(response) {
		        alert(response.message); // 성공 메시지 표시
		        window.location.href = '/myapp/main.do';
		    },
		    error: function(xhr, status, error) {
		        alert("회원가입 처리 중 에러가 발생했습니다.");
		    }
        });

        e.preventDefault(); // 폼 기본 제출 방지
    });
});