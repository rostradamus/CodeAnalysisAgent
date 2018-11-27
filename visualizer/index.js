
var diameter = 1500,
    radius = diameter / 2,
    innerRadius = radius - 120;

var cluster = d3.cluster()
    .size([360, innerRadius]);

var line = d3.radialLine()
    .curve(d3.curveBundle.beta(0.85))
    .radius(function(d) { return d.y; })
    .angle(function(d) { return d.x / 180 * Math.PI; });




var svg = d3.select("body").append("svg")
    .attr("width", diameter)
    .attr("height", diameter)
    .style("overflow", "visible")
    .append("g")
    .attr("transform", "translate(" + 1000 + "," + 800 + ")");


var legend = d3.select("body").append("div")

var link = svg.append("g").selectAll(".link"),
    node = svg.append("g").selectAll(".node");


var colorMap = new Map();
var usedColor = new Set();
var sortedLink=[];

d3.json("flare.json", function(error, classes) {


  if (error) throw error;

  var root = packageHierarchy(classes)
      .sum(function(d) { return d.size; });

 

  cluster(root);

  link = link
    .data(packageImports(root.leaves()))
    .enter().append("path")
      .each(function(d) { 
        d.source = d[0], d.target = d[d.length - 1];})
      .attr("class", "link")
      .attr("d", line)
      .attr("stroke", function(d){ 
        if(sortedLink.length == 0){
          return "black"
        }else{
        var hue = getLinkColorLevel(d.source.data.name+ " "+ d.target.data.name);
        console.log(hue);
        return getColor(hue);
      }
    });
      //   return 
      

  node = node
    .data(root.leaves())
    .enter().append("text")
      .attr("class", "node")
      .attr("dy", "0.31em")
      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + (d.y + 8) + ",0)" + (d.x < 180 ? "" : "rotate(180)"); })
      .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
      .attr("fill", function(d) {return colorMap.get(d.data.parent.name)})
      .text(function(d) { return d.data.key; });

  var legend = d3.select("body")
      .append("div")
      .style('position','absolute')
      .style('right', 0)
      .style('top', 0)
      .selectAll("div")
      .data(Array.from(colorMap.keys()))
      .enter()
      .append("div")
      .style('top', 0)
      .style('right', 100)
      .style('width', 500 + "px")
      .attr("transform", "translate(" + radius + "," + radius + ")");

  var legend_row = 
        legend.each(function(d){
        d3.select(this).append("color")
        .attr("class", "color")
        .text(function (d) { 
          return d + ": "})
        .each(function(d){
          d3.select(this).append("svg")
          .attr("width", 50)
          .attr("height", 50)
          .append("rect")
          .attr("x", 10)
          .attr("y", 25)
          .attr("width", 25)
          .attr("height", 25)
          .style("fill",  function(d){
             return colorMap.get(d)})
      })
    });


});

function sortObject(obj) {
    var arr = [];
    for (var prop in obj) {
        if (obj.hasOwnProperty(prop)) {
            arr.push({
                'key': prop,
                'value': obj[prop]
            });
        }
    }
    arr.sort(function(a, b) { return b.value - a.value; });
    //arr.sort(function(a, b) { a.value.toLowerCase().localeCompare(b.value.toLowerCase()); }); //use this to sort as strings
    return arr; // returns array
}
// Lazily construct the package hierarchy from class names.
function packageHierarchy(classes) {
  var map = {};

  function find(name, data) {
    var node = map[name], i;
    if (!node) {
      node = map[name] = data || {name: name, children: []};
      if (name.length) {
        node.parent = find(name.substring(0, i = name.lastIndexOf(".")));
        node.parent.children.push(node);
        node.key = name.substring(i + 1);
      }

      if(node.parent && !colorMap.has(node.parent.name)){
        var color = getRandomColor();
        while(usedColor.has(color)){
          color = getRandomColor();
        }
        if(node.parent.name != "")colorMap.set(node.parent.name, color);
        // console.log(colorMap.get(node.parent.name));
      }


    }
    return node;
  }

  classes.forEach(function(d) {
    // console.log(find(d.name,d));
    find(d.name, d);
    // console.log(d.name);
  });
  // console.log(d3.hierarchy(map[""]));

  return d3.hierarchy(map[""]);
}

function getColor(value){
    //value from 0 to 1
    var hue=((1-value)*120).toString(10);
    return ["hsl(",hue,",100%,50%)"].join("");
}

function getLinkColorLevel(link){
  console.log(sortedLink);
  console.log(link);
  var i = sortedLink.findIndex(x=> x.key == link);
  console.log(i);
  return (Math.round(i * 100.0 / (sortedLink.length-1)  ) / 100);

}

function getRandomColor() {
  var letters = '0123456789ABCDEF';
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

// Return a list of imports for the given array of nodes.
function packageImports(nodes) {
  var map = {},
      imports = [],
      linkMap = {};
      isStatic = 1;
  // Compute a map from name to node.
  nodes.forEach(function(d) {
    map[d.data.name] = d;
  });

  // For each import, construct a link from the source to target node.
  nodes.forEach(function(d) {
    if (d.data.imports) d.data.imports.forEach(function(i) {
      if(i.counter > 1) isStatic = 0;
      linkMap[d.data.name + " " + i.name]=i.counter;
      imports.push(map[d.data.name].path(map[i.name]));
    });
  });

  if(!isStatic)sortedLink = sortObject(linkMap);
 return imports;

}