$('#nav_menu li').click(function(e) {
	$('#nav_menu li.active').removeClass('active');
  var $this = $(this);
  if (!$this.hasClass('active')) {
    $this.addClass('active');
  }
  e.preventDefault();
});

