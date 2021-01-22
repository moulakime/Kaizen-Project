/**
 * 
 */

$('#first_name').on('keyup',function () {
   if($('#first_name').val().length > 2){
	   $('#fl_name').css({"border-color": "#99cc99", 
           "border-width":"2px", 
           "border-style":"solid"});
	   $('#success_message').text("");
	   $('#success_message').text("\u2713 We're good now.");
   	   $('#success_message').css("color","#99cc99");
    }else
    	{
    	$('#fl_name').css({"border-color": "#ff6666", 
            "border-width":"2px", 
            "border-style":"solid"});
    	$('#success_message').text("");
    	$('#success_message').text("\u2715 Two fields are required.");
    	$('#success_message').css("color","#ff6666");
    	}
});
$('#last_name').on('keyup', function () {
	   if($('#last_name').val().length > 2){
		   $('#fl_name').css({"border-color": "#99cc99", 
	           "border-width":"2px", 
	           "border-style":"solid"});
		   $('#success_message').text("");
		   $('#success_message').text("\u2713 We're good now.");
	   	   $('#success_message').css("color","#99cc99");
	    }else
	    	{
	    	$('#fl_name').css({"border-color": "#ff6666", 
	            "border-width":"2px", 
	            "border-style":"solid"});
	    	$('#success_message').text("");
	    	$('#success_message').text("\u2715 Two fields are required.");
	    	$('#success_message').css("color","#ff6666");
	    	}
	});
$('#job_title').on('keyup',function () {
	   if($('#job_title').val().length > 1){
		   $('#job_supervisor').css({"border-color": "#99cc99", 
	           "border-width":"2px", 
	           "border-style":"solid"});
		   $('#job_sup_message').text("");
		   $('#job_sup_message').text("\u2713 We're good now.");
	   	   $('#job_sup_message').css("color","#99cc99");
	    }else
	    	{
	    	$('#job_supervisor').css({"border-color": "#ff6666", 
	            "border-width":"2px", 
	            "border-style":"solid"});
	    	$('#job_sup_message').text("");
	    	$('#job_sup_message').text("\u2715 Two fields are required.");
	    	$('#job_sup_message').css("color","#ff6666");
	    	}
	});
$('#supervisor').on('keyup', function () {
		var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		   if($('#supervisor').val().length > 2 && filter.test($('#supervisor').val()) ){
			   $('#job_supervisor').css({"border-color": "#99cc99", 
		           "border-width":"2px", 
		           "border-style":"solid"});
			   $('#job_sup_message').text("");
			   $('#job_sup_message').text("\u2713 We're good now.");
		   	   $('#job_sup_message').css("color","#99cc99");
		    }else
		    	{
		    	$('#job_supervisor').css({"border-color": "#ff6666", 
		            "border-width":"2px", 
		            "border-style":"solid"});
		    	$('#job_sup_message').text("");
		    	$('#job_sup_message').text("\u2715 This is not an email.");
		    	$('#job_sup_message').css("color","#ff6666");
		    	}
		});
/*$('#email').on('keyup', function () {
		var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		   if(filter.test($('#email').val()) ){
			   $('#email_div').css({"border-color": "#99cc99", 
		           "border-width":"2px", 
		           "border-style":"solid"});
			   $('#email_message').text("");
			   $('#email_message').text("\u2713 We're good now.");
		   	   $('#email_message').css("color","#99cc99");
		    }else
		    	{
		    	$('#email_div').css({"border-color": "#ff6666", 
		            "border-width":"2px", 
		            "border-style":"solid"});
		    	$('#email_message').text("");
		    	$('#email_message').text("\u2715 This is not an email.");
		    	$('#email_message').css("color","#ff6666");
		    	}
		});*/
$('#password-confirm').on('keyup', function () {
	var pass1 = $('#password').val();
	var pass_confirm = $('#password-confirm').val();
    if (pass1 != pass_confirm) {
    	$('#re_pass').css({"border-color": "#ff6666", 
            "border-width":"2px", 
            "border-style":"solid"});
    	$('#pass').css({"border-color": "#ff6666", 
            "border-width":"2px", 
            "border-style":"solid"});
    	$('#message').text("\u2715 Your passwords don't match.");
    	$('#message').css("color","#ff6666");
    } else if (pass1.length < 4 ||  pass_confirm.length < 4)
    	{
    	$('#re_pass').css({"border-color": "#ff6666", 
            "border-width":"2px", 
            "border-style":"solid"});
    	$('#pass').css({"border-color": "#ff6666", 
            "border-width":"2px", 
            "border-style":"solid"});
    	$('#message').text("\u2715 password must be at least 4 digits.");
    	$('#message').css("color","#ff6666");
    	}else {
    	$('#re_pass').css({"border-color": "#99cc99", 
            "border-width":"2px", 
            "border-style":"solid"});
    	$('#pass').css({"border-color": "#99cc99", 
            "border-width":"2px", 
            "border-style":"solid"});
    	$('#message').text("\u2713 Your passwords are matched.");
    	$('#message').css("color","#99cc99");
    }
});

/* Function check if immatricule exist -BEGIN- */

$(function(){
    $("#immatricule").on("keyup",function(){
        var immatricule = $('#immatricule').val();
        if(immatricule == "")
    	{
    	$("#immatricule_status").html("");
    	}
             $.ajax({
                type: "GET",
                url: "/exist/"+immatricule,
                success: function(msg){

                    $("#immatricule_status").html(msg);

                    }
                });

    });
 });

/* Function check if immatricule exist -END- */

/* Function check if email exist -BEGIN- */

$(function(){
    $("#email").on("keyup",function(){
        var email = $('#email').val();
        if(email == "")
    	{
    	$("#status").html("");
    	}
             $.ajax({
                type: "GET",
                url: "/exist_email/"+email,
                success: function(msg){

                    $("#status").html(msg);

                    }
                });

    });
 });

/* Function check if email exist -END- */

/* Function check if email is for user connected -BEGIN- */

$(function(){
    $("#receipent").on("keyup",function(){
        var receipent = $('#receipent').val();
        if(receipent == "")
        	{
        	$("#status").html("");
        	}
             $.ajax({
                type: "GET",
                url: "/email_not_allowed/"+receipent,
                success: function(msg){

                    $("#status").html(msg).fadeIn();

                    }
                });

    });
 });

/* Function check if email is for user connected -END- */

/* Function list admin auto-complete -BEGIN- */

$(function(){
    $("#receipent").on("keydown",function(){
        var receipent = $('#receipent').val();
        if(receipent == "")
        	{
        	$("#receipentList").html("");
        	}
             $.ajax({
                type: "GET",
                url: "/get_admins_email/"+receipent,
                success: function(msg){

                    $("#receipentList").html(msg).fadeIn();

                    }
                });

    });
 });

/* Function list admin auto-complete -END- */

/* Function filter by department -BEGIN- */

/*$(function(){
    $("generate").on("click",function(){
        var department = $('#department').val();
        if(department == "")
        	{
        	$("tbody").html("No Data Found");
        	}
             $.ajax({
                type: "GET",
                url: "/reporting/"+department,
                success: function(msg){

                    $("tbody").html(msg);

                    }
                });

    });
 });*/

/* Function filter by department -END- */




/* ------------------------------------------------------------------------------ */

window.setTimeout(function() {
    $(".alert-warning").fadeTo(500, 0).slideUp(500, function(){
        $(this).remove(); 
    });
}, 3000);

window.setTimeout(function() {
    $(".alert-success").fadeTo(500, 0).slideUp(500, function(){
        $(this).remove(); 
    });
}, 3000);

window.setTimeout(function() {
    $(".alert-info").fadeTo(500, 0).slideUp(500, function(){
        $(this).remove(); 
    });
}, 3000);

window.setTimeout(function() {
    $(".alert-danger").fadeTo(500, 0).slideUp(500, function(){
        $(this).remove(); 
    });
}, 3000);

/* Password strength function */
function CheckPasswordStrength(password) {
	var password_strength = document.getElementById("strength");

	        //TextBox left blank.
	        if (password.length == 0) {
	            password_strength.innerHTML = "";
	            return;
	        }

	        //Regular Expressions.
	        var regex = new Array();
	        regex.push("[A-Z]"); //Uppercase Alphabet.
	        regex.push("[a-z]"); //Lowercase Alphabet.
	        regex.push("[0-9]"); //Digit.
	        regex.push("[$@$!%*#?&]"); //Special Character.

	        var passed = 0;

	        //Validate for each Regular Expression.
	        for (var i = 0; i < regex.length; i++) {
	            if (new RegExp(regex[i]).test(password)) {
	                passed++;
	            }
	        }

	        //Display status.
	        var color = "";
	        var strength = "";
	        switch (passed) {
	            case 0:
	            case 1:
	            case 2:
	                strength = "<b>Weak</b>";
	                color = "#ff6666";
	                break;
	            case 3:
	                 strength = "<b>Medium</b>";
	                color = "#ffc04c";
	                break;
	            case 4:
	                 strength = "<b>Strong</b>";
	                color = "#99cc99";
	                break;
	               
	        }
	        password_strength.innerHTML = strength;
	        password_strength.style.color = color;

	/*if(passed <= 2){
	         document.getElementById('show').disabled = true;
	        }else{
	            document.getElementById('show').disabled = false;
	        }*/

	    }
