/*var cy;

var data = {
  nodetype:[
    {id:'1', type:'gene'},
    {id:'2', type:'disease'},
    {id:'3', type:'protein'},
  ],

  edgetype:[
    {id:'1', type:'gene-disease'},
    {id:'2', type:'gene-protein'},
    {id:'3', type:'disease-protein'}
  ],
  nodes: [
    {data: {id: 'foo', kind: "gene" , rank: 1}},
    {data: {id: 'bar', kind: 'gene', rank: 10}},
    {data: {id: 'bar1',kind: 'gene', rank: 100}},
    {data: {id: 'bar2',kind: 'disease', rank: 1000}},
    {data: {id: 'bar3',kind: 'disease', rank: 100000}},
  ],
  edges: [
    {data: {id: 'foo-bar', source: 'foo', target: 'bar', weight: 0.9,kind:'gene-disease', rank:1}},
    {data: {id: 'foo-bar1', source: 'foo', target: 'bar1', weight: 0.9, rank:1}},
    {data: {id: 'foo-bar2', source: 'foo', target: 'bar2', weight: 0.9, rank:1}},
    {data: {id: 'foo-bar3', source: 'bar2', target: 'bar3', weight: 0.9, rank:1}},
  ]
};


var data2 = {
  nodes: [
    {data: {id: 'foo', kind: "gene" , rank: 1}},
    {data: {id: 'bar', kind: 'gene', rank: 2}},
    {data: {id: 'bar1',kind: 'gene', rank: 4}},

  ],
  edges: [
    {data: {id: 'foo-bar', source: 'foo', target: 'bar', weight: 0.9,kind:'gene-disease'}},
    {data: {id: 'foo-bar1', source: 'foo', target: 'bar1', weight: 0.9}},
    {data: {id: 'foo-bar2', source: 'foo', target: 'bar2', weight: 0.9}},
    {data: {id: 'foo-bar3', source: 'bar2', target: 'bar3', weight: 0.9}},
  ]
};

$(function(){ // on dom ready

  cy = cytoscape({
    container: document.getElementById('cy'),

    style: [{"selector":"core","style":{"selection-box-color":"#AAD8FF","selection-box-border-color":"#8BB0D0","selection-box-opacity":"0.5"}},{"selector":"node","style":{"width":"mapData(score, 0, 0.006769776522008331, 20, 60)","height":"mapData(score, 0, 0.006769776522008331, 20, 60)","content":"data(name)","font-size":"12px","text-valign":"center","text-halign":"center","background-color":"#555","text-outline-color":"#555","text-outline-width":"2px","color":"#fff","overlay-padding":"6px","z-index":"10"}},{"selector":"node[?attr]","style":{"shape":"rectangle","background-color":"#aaa","text-outline-color":"#aaa","width":"16px","height":"16px","font-size":"6px","z-index":"1"}},{"selector":"node[?query]","style":{"background-clip":"none","background-fit":"contain"}},{"selector":"node:selected","style":{"border-width":"6px","border-color":"#AAD8FF","border-opacity":"0.5","background-color":"#77828C","text-outline-color":"#77828C"}},{"selector":"edge","style":{"curve-style":"haystack","haystack-radius":"0.5","opacity":"0.4","line-color":"#bbb","width":"mapData(weight, 0, 1, 1, 8)","overlay-padding":"3px"}},{"selector":"node.unhighlighted","style":{"opacity":"0.2"}},{"selector":"edge.unhighlighted","style":{"opacity":"0.05"}},{"selector":".highlighted","style":{"z-index":"999999"}},{"selector":"node.highlighted","style":{"border-width":"6px","border-color":"#AAD8FF","border-opacity":"0.5","background-color":"#394855","text-outline-color":"#394855","shadow-blur":"12px","shadow-color":"#000","shadow-opacity":"0.8","shadow-offset-x":"0px","shadow-offset-y":"4px"}},{"selector":"edge.filtered","style":{"opacity":"0"}},{"selector":"edge[group=\"coexp\"]","style":{"line-color":"#d0b7d5"}},{"selector":"edge[group=\"coloc\"]","style":{"line-color":"#a0b3dc"}},{"selector":"edge[group=\"gi\"]","style":{"line-color":"#90e190"}},{"selector":"edge[group=\"path\"]","style":{"line-color":"#9bd8de"}},{"selector":"edge[group=\"pi\"]","style":{"line-color":"#eaa2a2"}},{"selector":"edge[group=\"predict\"]","style":{"line-color":"#f6c384"}},{"selector":"edge[group=\"spd\"]","style":{"line-color":"#dad4a2"}},{"selector":"edge[group=\"spd_attr\"]","style":{"line-color":"#D0D0D0"}},{"selector":"edge[group=\"reg\"]","style":{"line-color":"#D0D0D0"}},{"selector":"edge[group=\"reg_attr\"]","style":{"line-color":"#D0D0D0"}},{"selector":"edge[group=\"user\"]","style":{"line-color":"#f0ec86"}}],
    elements: data});

  var params = {
    name: 'cola',
    nodeSpacing: 20,
    edgeLengthVal: 10,
    animate: true,
    randomize: false,
    maxSimulationTime: 1500
  };
  var layout = makeLayout();
  var running = false;

  cy.on('layoutstart', function(){
    running = true;
  }).on('layoutstop', function(){
    running = false;
  });
  
  layout.run();

  var $config = $('#config');
  var $btnParam = $('<div class="param"></div>');
  $config.append( $btnParam );

  var sliders = [
    {
      label: 'Edge length',
      param: 'edgeLengthVal',
      min: 1,
      max: 200
    },

    {
      label: 'Node spacing',
      param: 'nodeSpacing',
      min: 1,
      max: 50
    }
  ];

  var buttons = [
    {
      label: '<i class="fa fa-random"></i>',
      layoutOpts: {
        randomize: true,
        flow: null
      }
    },

    {
      label: '<i class="fa fa-long-arrow-down"></i>',
      layoutOpts: {
        flow: { axis: 'y', minSeparation: 30 }
      }
    }
  ];

  sliders.forEach( makeSlider );

  buttons.forEach( makeButton );

  function makeLayout( opts ){
    params.randomize = false;
    params.edgeLength = function(e){ return params.edgeLengthVal / e.data('weight'); };

    for( var i in opts ){
      params[i] = opts[i];
    }

    return cy.makeLayout( params );
  }

  function makeSlider( opts ){
    var $input = $('<input></input>');
    var $param = $('<div class="param"></div>');

    $param.append('<span class="label label-default">'+ opts.label +'</span>');
    $param.append( $input );

    $config.append( $param );

    var p = $input.slider({
      min: opts.min,
      max: opts.max,
      value: params[ opts.param ]
    }).on('slide', _.debounce( function(){
      params[ opts.param ] = p.getValue();

      layout.stop();
      layout = makeLayout();
      layout.run();
    }, 50 ) ).data('slider');
  }

  function makeButton( opts ){
    var $button = $('<button class="btn btn-default">'+ opts.label +'</button>');
    
    $btnParam.append( $button );

    $button.on('click', function(){
      layout.stop();

      if( opts.fn ){ opts.fn(); }

      layout = makeLayout( opts.layoutOpts );
      layout.run();
    });
  }

}); // on dom ready
*/
function chenodeclick(o)
{


  if(o.checked == true)
  {
    cy.$("node[type = '"+o.value+"']").css({'visibility': "visible"});
  }
  else
  {
    cy.$("node[type = '"+o.value+"']").css({'visibility': "hidden"});
  }
}

function cheedgeclick(o)
{

  if(o.checked == true)
  {
    cy.$("edge[type = '"+o.value+"']").css({'display': "element"});
  }
  else
  {
    cy.$("edge[type = '"+o.value+"']").css({'display': "none"});
  }

}

/*

/!*element*!/
function selectclick(o)
{
  cy.elements("node[rank<100]");
}
*/



/*
/!*splice*!/
function selectclick(o)
{
  var subdata = data;
  for (var i = 0 ; i<subdata.nodes.length ; i++)
  {
    if (subdata.nodes[i].data.rank > o.value)
    {
      subdata.nodes.splice(i,1);
    }
  }

  cy.load(subdata);
  cy.layout({name: "random"});*!/
}
*/

/*

/!*隐藏*!/
function selectclick(o)
{
  cy.$("node[rank <= '"+o.value+"']").css({'visibility': "visible"});
  cy.$("node[rank > '"+o.value+"']").css({'visibility': "hidden"});
}
*/

/*构建新temp*/
function selectclick(o)
{
  var no = 0;
  var temp = {nodes: [], edges: []};
  for(var i = 0;i<globaljson.map.nodes.length ;i++)
  {
    if(globaljson.map.nodes[i].data.rank<= o.value)
    {
      temp.nodes.push(globaljson.map.nodes[i]);
      no++;
    }
  }


  for(var i = 0;i<globaljson.map.edges.length ;i++)
  {
    temp.edges.push(globaljson.map.edges[i]);
  }

  cy.load(temp);
  cy.layout({name: "circle"});
}

/*
/!*删除副本, delete*!/
function selectclick(o)
{
  var subdata = data;
  var len = subdata.nodes.length;
  var no = 0;
  for (var i = 0; i < len; i++) {
    if (subdata.nodes[no].data.rank > o.value) {
      subdata.delete(subdata.nodes[no]);
    }
    else {
      no++;
    }
  }
  cy.load(subdata);
  cy.layout({name: "random"});
}
*/



/*
/!*删除副本,splice*!/
function selectclick(o)
{
  for (var i = 0 ; i<subdata.nodes.length ; i++)
  {
    if (subdata.nodes[i].data.rank > o.value)
    {
      subdata.nodes.splice(i,1);
    }
  }
  cy.load(subdata);
  cy.layout({name: "random"});
}
*/


$("select[name=layout]").change(function(){
  var layout_name = $(this).val();
  cy.layout({name: layout_name});
})


$(function() {
  FastClick.attach( document.body );
});
