/**
 * 
 */
(function() {
  $(function() {

    var editor, mobileToolbar, toolbar;
    toolbar = ['title', 'bold', 'italic', 'underline', 'strikethrough', 'color', '|', 'ol', 'ul', 'blockquote', '|', 'link', 'image', 'hr'];
    return editor = new Simditor({
      textarea: $('#txt-content'),
      placeholder: '这里输入文字...',
      toolbar: toolbar,
      pasteImage: true,
    });
  });

}).call(this);