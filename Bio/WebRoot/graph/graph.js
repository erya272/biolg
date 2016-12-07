var display_flag = true;
var display_neighbor_flag = true;

$(function() {
	'use strict';
	Ladda.bind('#reload');
	var spinner = new Spinner();

	function show_mask() {
		var target = document.getElementById('main-control');
		$("#mask").show();
		spinner.spin(target);
	}

	function hide_mask() {
		$("#mask").hide();
		spinner.stop();
	}

	$(document).ajaxStart(function() {
		show_mask();
	});

	$(document).ajaxStop(function() {
		hide_mask();
	});

	$('[data-tip!=""]').qtip({
		content : {
			attr : 'data-tip'
		},
		hide : {
			fixed : true,
			delay : 300
		},
		position : {
			target : 'mouse',
			adjust : {
				mouse : false
			}
		},
		style : {}
	});

	var layout_name = 'concentric';

	var turn = 0;
	var last_gene = "";

	var locked_gene = Array();
	var selected_gene = Array();

	// load data on page init

	var data2 = {
		nodes : [ {
			data : {
				id : '1',
				label : 'test',
				foo : 'test'
			}
		} ],

	};

	var cy = cytoscape({
		container : document.getElementById('cy'),
		// elements: data,
		ready : function() {
			// $('#cy').cytoscapeNavigator();
		},
		style : cytoscape.stylesheet().selector('node').css({
			// 'content': 'data(name)',
			'text-valign' : 'center',
			// 'text-halign': 'right',
			'color' : 'black',
			'text-outline-width' : 1.5,
			'text-outline-color' : '#888',
			'width' : 20,
			'height' : 20,
			'min-zoomed-font-size' : 1,
			'font-size' : 1
		})

		// .selector('label')
		// .css({
		// 'font-size': 40
		// })
		.selector('edge').css({
			'target-arrow-shape' : 'none',
			// 'line-color' : 'mapData(weight, 0.1, 0.95, green, red)',
			'line-color' : '#99CCFF',
			'opacity' : 0.95,
		// 'width' : 'mapData(weight, 0.1, 1.0, 1, 5)',
		// 'content': 'data(id)'
		}).selector(':selected').css({
			'background-color' : 'blue',
			'line-color' : 'blue',
			'target-arrow-color' : 'black',
			'source-arrow-color' : 'black'
		}).selector('.faded').css({
			'opacity' : 0.25,
			'text-opacity' : 0
		}),
		layout : {
			name : layout_name
		},
		minZoom : 0.4,
		maxZoom : 2,
	});

	cy.panzoom();

	cy.cxtmenu({
		commands : [
				{
					content : '<span class="fa fa-plus fa-2x"></span>',
					select : function() {
						console.log('plus');
						var pre_content = $.trim($("#gene").val());
						var genes = pre_content.split('\n');
						if (genes.indexOf(this.data('id')) >= 0) {
							return;
						}
						if (genes.length >= 3 && genes.length <= 5) {
							$("#gene").attr('rows', genes.length + 1);
						}
						if (pre_content.length < 2) {
							pre_content = "";
						} else {
							pre_content += '\n';
						}
						$("#gene").val(pre_content + this.data('id'));
						console.log(pre_content);
						console.log($.trim(pre_content));
					}
				},
				{
					content : '<span class="fa fa-flag fa-2x"></span>',
					select : function() {
						var selector = "node[id='" + this.data('id') + "']";
						var s = cy.$(selector)[0];
						if (s.css('background-color') == 'green')
							s.css('background-color', '#777');
						else
							s.css('background-color', 'green');
						console.log('flag');
					}
				},
				{
					content : '<span class="fa fa-lock fa-2x"></span>',
					select : function() {
						console.log('lock');
						var g = this.data('id');
						for ( var i = 0; i < locked_gene.length; i++) {
							if (locked_gene[i] == g) {
								console.log('has item in locked_gene');
								return;
							}
						}
						;
						locked_gene.push(g);
						cy.$("node[id='" + g + "']")[0].css('background-color',
								'red');
						on_lock_unlock();

						console.log(locked_gene);
					}
				},
				{
					content : '<span class="fa fa-unlock fa-2x"></span>',
					select : function() {
						console.log('unlock');
						var g = this.data('id');
						for ( var i = 0; i < locked_gene.length; i++) {
							if (locked_gene[i] == g) {
								locked_gene.splice(i, 1);
								cy.$("node[id='" + g + "']")[0].css(
										'background-color', '#777');

								on_lock_unlock();

								break;
							}
						}
						;
						if (locked_gene.length == 0) {
							cy.$("node").css({
								'opacity' : 1
							});
							cy.$("edge").css({
								'opacity' : 0.95
							});
						}
						console.log(locked_gene);
					}
				}, {
					content : '<span class="fa fa-info fa-2x"></span>',
					select : function() {
						console.log('info');
						get_gene_info(this.data('id'));
					}
				} ]
	});

	function on_lock_unlock() {
		cy.$("node").css({
			'opacity' : 0.50
		});
		cy.$("edge").css({
			'opacity' : 0.05
		});

		for ( var i = 0; i < locked_gene.length; i++) {
			highlight_neighbor(locked_gene[i]);
		}
		;

		for ( var i = 0; i < selected_gene.length; i++) {
			highlight_neighbor(selected_gene[i]);
		}
		;
	}

	$("#select").click(
			function() {
				var genes = $("#gene").val().split('\n');
				console.log(genes);
				for ( var i = 0; i < selected_gene.length; i++) {
					cy.$("node[id='" + selected_gene[i] + "']")[0].css(
							'background-color', '#777');
				}
				;
				selected_gene = [];
				for ( var k = 0; k < genes.length; k++) {
					var g = genes[k];
					if (g.length == 0)
						continue;
					console.log(g);
					selected_gene.push(g);
					cy.$("node[id='" + g + "']")[0].css('background-color',
							'yellow');
				}
				;
				console.log(selected_gene);
				on_lock_unlock();
			});

	cy.on("zoom", function(e) {
		// return;
		console.log(e);
		var zoom_level = cy.zoom();
		console.log(zoom_level);
		// zoom_level = 1 + 0.01 * zoom_level;
		var font_size = 10 / zoom_level;
		var node_width = 25 / zoom_level;
		var node_height = 25 / zoom_level;
		console.log(node_width);

		var edge_min_width = 0.2 / zoom_level;
		var edge_max_width = 3 / zoom_level;

		cy.style().selector("node").css({
			'font-size' : font_size,
			'width' : node_width,
			'height' : node_height
		}).selector("edge").css({
			// 'width': 'mapData(weight, 0.5, 1.0, '+ edge_min_width
			// +','+edge_max_width+')'
			'width' : edge_max_width
		}).update();
		// cy.forceRender();
	});

	cy.on("click", function(e) {
		console.log(e);
		if (e.cyTarget == cy) {
			console.log('cy');
			cy.$("node").css({
				'opacity' : 1
			});
			cy.$("edge").css({
				'opacity' : 0.95
			});
			return false;
		} else if (!e.cyTarget.isNode()) {
			return false;
		}

		cy.$("node").css({
			'opacity' : 0.50
		});
		cy.$("edge").css({
			'opacity' : 0.05
		});

		var g = e.cyTarget.id();
		highlight_neighbor(g);
		fill_neighbor_list(g);

		for ( var i = 0; i < locked_gene.length; i++) {
			highlight_neighbor(locked_gene[i]);
		}
		;

		get_gene_info(g);
	});

	function highlight_neighbor(g) {
		if (display_neighbor_flag) {
			var neighbors = cy.$("node[id='" + g + "']")[0]
					.closedNeighborhood();
		} else {
			var neighbors = cy.$("node[id='" + g + "']");
		}
		neighbors.css({
			'opacity' : 1
		});
		neighbors.addClass('hightlight');
		for ( var i = 0; i < selected_gene.length; i++) {
			cy.$("edge[source='" + g + "'][target='" + selected_gene[i] + "']")
					.css({
						'opacity' : 1
					});
		}
	}

	function fill_neighbor_list(g) {
		var neighbors = cy.$("node[id='" + g + "']")[0].neighborhood();
		console.log(neighbors);
		var list = "";
		var info = "";
		var node = cy.$("node[id='" + g + "']")[0];
		info += "link: " + node.data('link') + '\n' + "abstract: "
				+ node.data('abstracts') + '\n' + "database: "
				+ node.data('database');

		for ( var i = 0; i < neighbors.length; i++) {
			if (neighbors[i].isNode()) {
				list += neighbors[i].data('id') + '\n';
			}
		}
		;
		console.log(list);

		$("#neighborhood-list-text").val(list);

		$("#nodeinfo-list-text").val(info).parent().show();
	}

	function get_gene_info(g) {
		if (g == last_gene) {
			return;
		} else {
			last_gene = g;
		}
		$
				.getJSON(
						'$config.root/result/gene',
						{
							gene : g,
							run_id : '$run_id'
						},
						function(data) {
							console.log(data);
							var ann_info = $("#ann-" + turn);

							ann_info
									.find("h3")
									.html(
											"<a title='click for gene details in NCBI' href='http://www.ncbi.nlm.nih.gov/gene/?term="
													+ g
													+ "' target='_blank'>"
													+ g + "</a>");
							ann_info.find(".desc").html(data.gene_desc);

							var tbl = ann_info.find('table');
							tbl.html("");

							var tr = "<tr><th>GO id</td>"
									+ "<th>GO name</th></tr>";
							tbl.append(tr);
							for ( var i = 0; i < data.anns.length; i++) {
								var ann = data.anns[i];
								var tr = "<tr><td><a title='click for term details in Amigo2' href='http://amigo.geneontology.org/amigo/term/"
										+ ann.go_id
										+ "' target='_blank'>"
										+ ann.go_id
										+ "</a></td>"
										+ "<td>"
										+ ann.go_name + "</td></tr>";
								tbl.append(tr);
							}
							;

							if (!ann_info.is(':visible'))
								ann_info.show();

							ann_info.prependTo("#annotation");

							turn = (turn + 1) % 2;
							console.log(turn);
						});
	}

	$("#neighborhood-list-text").hover(function() {
		$(this).select();
	});

	$.getJSON("getData.action", function(data) {
		console.log(data);
		cy.load(data.map);
		// cy.load(data.map);
		refresh_style(data.map.types);
		refresh_tip();
		// cy.load(graphelements);
	});

	function refresh_style(types) {
		// 循环外层用type的个数来控制
		// alert(cy.nodes()[0].data("id"));
		var color = [ '#FFFFCC', '#CCFFFF', '#FFCCCC', '#99CCCC', '#FFCC99',
				'#FFCCCC', '#FF9999', '#FF6666', '#CC9966', '#CC9999',
				'#FF6666', '#CCCC00', '#0066CC', '#CCCC99', '#FFCC99' ];
		for ( var i = 0; i < types.nodetype.length; i++) {
			cy.style()
					.selector("node[type = '" + types.nodetype[i].type + "']")
					.css('background-color', color[i]);
		}
		// 添加length个颜色的按钮
		//	

		// cy.style().selector('node[]').css('line-color', 'mapData(weight,
		// '+e.value+', 0.9, blue, red').update();
		// console.log('mapData(weight, '+e.value+', 0.95, blue, red)');

	}

	function refresh_tip() {
		cy.nodes().qtip({
			content : function() {
				return this.id();
			},
			show : {
				event : 'mouseover'
			},
			hide : {
				event : 'mouseout'
			},
			style : {
				classes : 'light',
				tip : {
					width : 16,
					height : 8
				}
			},
			position : {
				target : 'mouse',
				viewport : $(window),
				at : 'bottom left',
				my : 'top left',
				adjust : {
					x : 50,
					y : 100
				}
			}
		});
		cy.edges().qtip({
			content : function() {
				return this.data('id');
			},
			show : {
				event : 'mouseover'
			},
			hide : {
				event : 'mouseout'
			},
			style : {
				classes : 'qtip-tipsy',
				tip : {
					width : 16,
					height : 8
				}
			}
		});
	}

	$("#threshold-slider").slider({});

	$("#threshold-slider").on(
			'slide',
			function(e) {
				$("#threshold").val(e.value);
				cy.style().selector('edge').css('line-color',
						'mapData(weight, ' + e.value + ', 0.9, blue, red')
						.update();
				console
						.log('mapData(weight, ' + e.value
								+ ', 0.95, blue, red)');
				// $("#reload").trigger('click');
			});

	$("#threshold").blur(function() {
		$("#reload").trigger('click');
	});
	$("#threshold-slider").mouseup(function() {
		$("#reload").trigger('click');
	});

	$("#threshold").bind('keypress', function(event) {
		if (event.keyCode == "13") {
			$("#reload").trigger('click');
		}
	});

	$("select[name=layout]").change(function() {
		layout_name = $(this).val();
		cy.layout({
			name : layout_name
		});
	});

	$("#threshold-slider").change(function() {
		alert($(this).val());
	});

	$("input[name=edge_label]").change(function() {
		var content = "";
		if ($(this).val() == 'on') {
			content = "data(weight)";
		} else {
			content = "";
		}
		cy.style().selector('edge').css({
			"content" : content
		}).update();
	});

	$("#show-label").click(function() {
		if (display_flag) {
			cy.nodes().trigger('mouseover');
			display_flag = false;
		} else {
			cy.nodes().trigger('data');
			display_flag = true;
		}
	});

	$("#show-neighbor").click(function() {
		if (display_neighbor_flag) {
			display_neighbor_flag = false;
			on_lock_unlock();
		} else {
			display_neighbor_flag = true;
			on_lock_unlock();
		}
	});

	$("input[name=node_label]").change(function() {
		var content = "";
		if ($(this).val() == 'on') {
			// content = "data(id)";
			var nodes = cy.nodes(':selected');
			console.log(nodes);
			// if(nodes.length > 0){
			// nodes.trigger('mouseover');
			// }else{
			cy.nodes().trigger('mouseover');
			// }
		} else {
			// content = "";
			cy.nodes().trigger('data');
		}
		// cy.style().selector('node').css({
		// "content" : content
		// }).update();
	});

	$("#download-png").click(
			function() {
				var png64 = cy.png({
					bg : 'white'
				});
				console.log(png64);

				// 加工image data，替换mime type
				png64 = png64.replace('image/png', 'image/octet-stream');

				/**
				 * 在本地进行文件保存
				 * 
				 * @param {String}
				 *            data 要保存到本地的图片数据
				 * @param {String}
				 *            filename 文件名
				 */
				var saveFile = function(data, filename) {
					var save_link = document.createElementNS(
							'http://www.w3.org/1999/xhtml', 'a');
					save_link.href = data;
					save_link.download = filename;

					var event = document.createEvent('MouseEvents');
					event.initMouseEvent('click', true, false, window, 0, 0, 0,
							0, 0, false, false, false, false, 0, null);
					save_link.dispatchEvent(event);
				};

				var filename = 'download_png_' + (new Date()).getTime()
						+ '.png';
				// download
				saveFile(png64, filename);
			});

	// download result file that only contain pair above threshold
	$("#download-result").click(function() {
		var old_href = $(this).attr('href');
		$(this).attr('href', old_href + '&threshold=' + $("#threshold").val());
	});

	$(".close-ann").click(function() {
		$(this).parent().hide();
	});

	$(".empty-text").click(function() {
		$("#gene").val("").attr('rows', 3);
	});

	var up_down_show_status = true;
	var down_up_show_status = true;

	$("#up-down").click(
			function() {
				if (up_down_show_status) {
					$("#main-control").animate(
							{
								top : '-270px'
							},
							'fast',
							function() {
								$(".up-down-icon").removeClass(
										"glyphicon-chevron-up").addClass(
										"glyphicon-chevron-down");
								$(".ui-cytoscape-panzoom").css('left', '0');
							});
					up_down_show_status = false;
				} else {
					$(".ui-cytoscape-panzoom").css('left', '350px');
					$("#main-control").animate(
							{
								top : '45px'
							},
							'fast',
							function() {
								$(".up-down-icon").removeClass(
										"glyphicon-chevron-down").addClass(
										"glyphicon-chevron-up");
							});
					up_down_show_status = true;
				}
			});

	$("#down-up").click(
			function() {
				if (down_up_show_status) {
					$(".bar-container").animate(
							{
								bottom : '-260px'
							},
							'fast',
							function() {
								$(".down-up-icon").removeClass(
										"glyphicon-chevron-down").addClass(
										"glyphicon-chevron-up");
							});
					down_up_show_status = false;
				} else {
					$(".bar-container").animate(
							{
								bottom : '0px'
							},
							'fast',
							function() {
								$(".down-up-icon").removeClass(
										"glyphicon-chevron-up").addClass(
										"glyphicon-chevron-down");
							});
					down_up_show_status = true;
				}
			});

	// $("select").select2();
	var select = $("#layout-select");
	// select.select2();
	/*
	 * $.getJSON('$config.root/result/sjson', {run_id:'$run_id'},
	 * function(data){ // var values = get_values(data); var values =
	 * data.edges; plot_histogram(values); });
	 */
	function get_values(data) {
		var vs = Array();
		for ( var i = 0; i <= data.edges.length - 1; i++) {
			// if(data.edges[i].data.weight==0) continue;
			vs[i] = data.edges[i].data.weight;
		}
		;
		console.log(vs);
		return vs;
	}

	$('.GO-term-amigo').qtip(
			{
				content : function() {
					return 'term details for "' + $(this).html()
							+ '" in Amigo website';
				}
			});

});