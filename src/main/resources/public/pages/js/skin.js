var skinData = [{
		name: 'Cerulean',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-cerulean.css'
	},
	{
		name: 'Cyborg',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-cyborg.css'
	},
	{
		name: 'Flatly',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-flatly.css'
	},
	{
		name: 'Journal',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-journal.css'
	},
	{
		name: 'Lumen',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-lumen.css'
	},
	{
		name: 'Paper',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-paper.css'
	},
	{
		name: 'Readable',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-readable.css'
	},
	{
		name: 'Sandstone',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-sandstone.css'
	},
	{
		name: 'Simplex',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-simplex.css'
	},
	{
		name: 'Slate',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-slate.css'
	},
	{
		name: 'Solar',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-solar.css'
	},
	{
		name: 'Spacelab',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-spacelab.css'
	},
	{
		name: 'United',
		value: '../../base/bootstrap/bootstrap-skin/bootstrap-united.css'
	}
];

$(function() {
	changeSkin();
	$(".changeSkin").click(function() {
		$('body').hide();
		changeSkin();
		setTimeout(function() {
		$('body').show();
	}, 200);
	});
	setTimeout(function() {
		$('body').show();
	}, 200);
});

function changeSkin() {
	var color = localStorage.boncColor;
	if(color) {
		$(".btn-success").removeClass('btn-success').addClass('btn-primary');
		if(color == 'red bodyColor') {
			$(".bootselect").attr('href', skinData[3].value);
		} else if(color == 'blue bodyColor') {
			$(".bootselect").attr('href', skinData[0].value);
		} else if(color == 'orange bodyColor') {
			$(".bootselect").attr('href', skinData[12].value);
		} else if(color == 'darkblue bodyColor') {
			$(".bootselect").attr('href', skinData[7].value);
		} else if(color == 'green bodyColor') {
			$(".btn-primary").removeClass('btn-primary').addClass('btn-success');
			$(".bootselect").attr('href', skinData[2].value);
		}
	} else {
		$(".bootselect").attr('href', skinData[0].value);
	}
}