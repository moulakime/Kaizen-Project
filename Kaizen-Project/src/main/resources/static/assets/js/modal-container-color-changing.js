$('#type_id').on('change' , function(){
  $value = $(this).val();
    if($value == 1){
      /*$('#modal-container_second').css("background-color" , "#ff4c4c");
      $('#modal_footer').css("background-color" , "#ff4c4c");
      $('.uk-placeholder').css("border","1px dashed black")
      $('.uk-button-default').css("border", "1px solid black");
      $('.uk-modal-footer').css("border-top", "1px solid black");*/
      $('#bande').css("background-color" , "#ff4c4c");
    }else if($value == 2){
      /*$('#modal-container_second').css("background-color" , "#ffff32");
      $('#modal_footer').css("background-color" , "#ffff32");
      $('.uk-placeholder').css("border","1px dashed black")
      $('.uk-button-default').css("border", "1px solid black");
      $('.uk-modal-footer').css("border-top", "1px solid black");*/
      $('#bande').css("background-color" , "#ffff32");
    }else if($value == 3){
      /*$('#modal-container_second').css("background-color" , "#66b266");
      $('#modal_footer').css("background-color" , "#66b266");
      $('.uk-placeholder').css("border","1px dashed black")
      $('.uk-button-default').css("border", "1px solid black");
      $('.uk-modal-footer').css("border-top", "1px solid black");*/
      $('#bande').css("background-color" , "#66b266");
    }else{
        /*$('#modal-container_second').css("background-color" , "#fff");
        $('#modal_footer').css("background-color" , "#fff");
        $('.uk-placeholder').css("border","1px dashed #e5e5e5")
        $('.uk-button-default').css("border", "1px solid #e5e5e5");
        $('.uk-modal-footer').css("border-top", "1px solid #e5e5e5");*/
        $('#bande').css("background-color" , "transparent");
    }

});
$('#mydata_table > tbody > tr').hover(function() {
    $(this).css("background","#e5e5e5");
    $(this).css("cursor","pointer");
}, function () {
    $(this).css("background","");
    $(this).css("cursor","context-menu");
  }
);
