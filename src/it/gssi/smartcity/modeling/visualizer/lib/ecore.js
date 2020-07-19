// custom shapes to visualise ecore diagrams
var ecore = function () {

	/**
	 * Utility functions
	 */
	var util = function() {

		// calculate with of text by using a temporal svg element
		//   mechanism extracted from joint.util.breakText
		var computeTextWidth = function(text, styles, opt) {

			opt = opt || {};
			styles = styles || {};

			var svgDocument = opt.svgDocument || V('svg').node;
			var textSpan = joint.V('tspan').node;
			var textElement = joint.V('text').attr(styles).append(textSpan).node;
			var textNode = document.createTextNode('');

			// Prevent flickering
			textElement.style.opacity = 0;
			// Prevent FF from throwing an uncaught exception when `getBBox()`
			// called on element that is not in the render tree (is not measurable).
			// <tspan>.getComputedTextLength() returns always 0 in this case.
			// Note that the `textElement` resp. `textSpan` can become hidden
			// when it's appended to the DOM and a `display: none` CSS stylesheet
			// rule gets applied.
			textElement.style.display = 'block';
			textSpan.style.display = 'block';

			textSpan.appendChild(textNode);
			svgDocument.appendChild(textElement);

			if (!opt.svgDocument) {
				document.body.appendChild(svgDocument);
			}

			textNode.data = text;
			var textWidth = textSpan.getComputedTextLength();

			if (opt.svgDocument) {
				// svg document was provided, remove the text element only
				svgDocument.removeChild(textElement);
			}
			else {
				// clean svg document
				document.body.removeChild(svgDocument);
			}

			return textWidth;
		};

		return {
			computeTextWidth : computeTextWidth
		}
	}();

	/**
	 * jointjs Paper diagram creation
	 */

	var createClassDiagram = function () {
		return new joint.dia.Paper({
			el: document.getElementById('paper'),
			model: graph,
			width: 10, // the real size should be updated with paper.fitToContent();
			height: 10,
			gridSize: 5
			, defaultAnchor: {
				name: 'perpendicular'
			}
			// https://resources.jointjs.com/docs/jointjs/v3.1/joint.html#routers.manhattan
			, defaultRouter: {
				name: 'manhattan',
				args: {
					step: 5,
					padding: 12
				}
			},
			restrictTranslate: true // prevent elements from moving outside the paper area
			, interactive: { labelMove: true }
			// , interactive: false // disables ALL interactions with the graph
		});
	}

	/**
	 * Nodes
	 */

	var EClass = joint.shapes.basic.Generic.define('ecore.EClass', {
		attrs: {
			'.eclass-name-rect': { 'stroke': 'black', 'stroke-width': 1, 'fill': '#fffcdc' },
			'.eclass-attrs-rect': { 'stroke': 'black', 'stroke-width': 1, 'fill': '#fffcdc' },
			'.eclass-methods-rect': { 'stroke': 'black', 'stroke-width': 1, 'fill': '#fffcdc' },

			// Note: for the calculateWidth function to work properly, the font details of these
			//   three elements (class name, attrs and methods) must be always the same
			// TODO: improve/merge common styles in better css classes
			'.eclass-name-text': {
				'ref': '.eclass-name-rect',
				'ref-y': .5,
				'ref-x': .5,
				'text-anchor': 'middle',
				'y-alignment': 'middle',
				'font-weight': 'bold',
				'fill': 'black',
				'font-size': 16, 'font-family': 'monospace'
			},
			'.eclass-attrs-text': {
				'ref': '.eclass-attrs-rect', 'ref-y': 7, 'ref-x': 7,
				'fill': 'black',
				'font-size': 14, 'font-family': 'monospace'
			},
			'.eclass-methods-text': {
				'ref': '.eclass-methods-rect', 'ref-y': 7, 'ref-x': 7,
				'fill': 'black',
				'font-size': 14, 'font-family': 'monospace'
			}
		},

		size: {}, // fill this size attribute for autolayout

		name: [],
		attributes: [],
		methods: []
	}, {
		// TODO: change to json parsing to improve performance
		// https://resources.jointjs.com/docs/jointjs/v3.1/joint.html#dia.Cell.markup
		markup: [
			'<g class="rotatable">',
			'<g class="scalable">',
			'<rect class="eclass-name-rect"/><rect class="eclass-attrs-rect"/><rect class="eclass-methods-rect"/>',
			'</g>',
			'<text class="eclass-name-text"/><text class="eclass-attrs-text"/><text class="eclass-methods-text"/>',
			'</g>'
		].join(''),

		// automatically called when creating an object
		initialize: function () {
			this.updateRectangles();
			joint.shapes.basic.Generic.prototype.initialize.apply(this, arguments);
		},

		getClassName: function () {
			return this.get('name');
		},

		updateRectangles: function () {

			var attrs = this.get('attrs');

			var rects = [
				{ type: 'name', text: this.getClassName() },
				{ type: 'attrs', text: this.get('attributes') },
				{ type: 'methods', text: this.get('methods') }
			];

			var rectWidth = this.calculateWidth(rects);
			var fontSize = attrs[".eclass-attrs-text"]["font-size"]

			var accumulatedHeight = 0;

			rects.forEach(function (rect) {

				var lines = Array.isArray(rect.text) ? rect.text : [rect.text];
				var rectHeight;
				// hide methods rectangle if it is empty (as usually)
				if (rect.type == "methods" && lines.length == 0) {
					rectHeight = 0;
				}
				else {
					rectHeight =
						lines.length * fontSize
						+ 2 * attrs[".eclass-attrs-text"]["ref-y"];
				}

				attrs['.eclass-' + rect.type + '-text'].text = lines.join('\n');
				attrs['.eclass-' + rect.type + '-rect'].height = rectHeight;
				attrs['.eclass-' + rect.type + '-rect'].width = rectWidth;
				attrs['.eclass-' + rect.type + '-rect'].transform = 'translate(0,' + accumulatedHeight + ')';

				accumulatedHeight += rectHeight;
			});

			// update size (required for autolayout)
			var size = this.get("size");
			size.width = rectWidth;
			size.height = accumulatedHeight;
		},

		calculateWidth: function (rects) {
			// taking class name attributes because font size is bigger
			var textStyle = {
				"font-size": this.attr(".eclass-name-text/font-size"),
				"font-family": this.attr(".eclass-name-text/font-family")
			};

			var maxWidth = 0;
			// this svg doc is created to improve performance of computeTextWidth
			var svgDocument = V('svg').node;
			document.body.appendChild(svgDocument);
			rects.forEach(function (rect) {
				var lines = Array.isArray(rect.text) ? rect.text : [rect.text];
				for (index = 0; index < lines.length; ++index) {
					var lineWidth = util.computeTextWidth(
						lines[index],
						textStyle,
						{svgDocument : svgDocument});
					if (lineWidth > maxWidth) {
						maxWidth = lineWidth;
					}
				}
			});
			document.body.removeChild(svgDocument);

			return maxWidth + 2 * this.attr(".eclass-attrs-text/ref-x");
		},

		setFillColor: function (fillColor) {
			var attrs = this.get("attrs");
			attrs ['.eclass-name-rect'].fill = fillColor;
			attrs ['.eclass-attrs-rect'].fill = fillColor;
			attrs ['.eclass-methods-rect'].fill = fillColor;
		}
	});

	var CustomTextBox = joint.shapes.standard.Rectangle.define('ecore.CustomTextBox', {
		attrs: {
			label: {
				textAnchor: 'left',
				textVerticalAnchor: 'middle',
				fontSize: 12,
				fontFamily: "monospace",
				refX: 5, // must be absolute because of setText() below
				refY: '50%'
			},
			body: {
				strokeWidth: 1
			}
		}
	}, {
		setText: function (newText) {
			var breakTextWidth = 300;
			var textStyle = {
				"font-size": this.attr("label/fontSize"),
				"font-family": this.attr("label/fontFamily")
			};

			// split the text into different lines (up to the allowed width)
			var brokenText = joint.util.breakText(
					newText, { width: breakTextWidth }, textStyle);
			this.attr("label/text", brokenText);

			// set the model size according to the text size
			var padding = 10;
			var lineVerticalSize = this.attr("label/fontSize");
			var lines = brokenText.split(/\n/)
			var numLines = lines.length

			var size = this.get("size");
			size.height = numLines * lineVerticalSize + padding;


			var maxWidth = 0;
			// this svg doc is created to improve performance of computeTextWidth
			var svgDocument = V('svg').node;
			document.body.appendChild(svgDocument);
			lines.forEach(function (line) {
				var lineWidth = util.computeTextWidth(
						line,
						textStyle,
						{ svgDocument : svgDocument});
				if (lineWidth > maxWidth) {
					maxWidth = lineWidth;
				}
			});
			document.body.removeChild(svgDocument);
			// Also add padding for each side
			size.width = maxWidth + this.attr("label/refX") * 2;
		},

		createLinkFrom: function (node) {
			var link = new DashedLink();
			link.source(node);
			link.target(this);
			return link;
		}
	});

	var Documentation = CustomTextBox.define("ecore.Documentation", {
		attrs: {
			body: {
				fill: "azure"
			}
		}
	});

	var Constraint = CustomTextBox.define("ecore.Constraint", {
		attrs: {
			body: {
				fill: "mistyrose"
			}
		}
	});

	var Critique = CustomTextBox.define("ecore.Critique", {
		attrs: {
			body: {
				fill: "lemonchiffon"
			}
		}
	});

	/*********
	 * Links *
	 *********/

	var CustomLink = joint.shapes.standard.Link.define("ecore.customLink", {
		attrs: {
			line: {
				targetMarker: null
			}
		}
	},
	{
		initTools: function (paper) {
			var linkView = this.findView(paper);
			linkView.addTools(new joint.dia.ToolsView({
				tools: [
					new joint.linkTools.Vertices(),
					new joint.linkTools.Segments(),
					new joint.linkTools.Boundary(),
					new joint.linkTools.SourceAnchor(),
					new joint.linkTools.TargetAnchor()
				]
			}));
			linkView.hideTools(); // made visible through paper mouse events
		}
	});

	var DashedLink = CustomLink.define("ecore.dashedLink", {
		attrs: {
			line: {
				stroke: "grey",
				strokeWidth: 1,
				strokeDasharray: "5 5",
				strokeDashoffset: 7.5,
			}
		}
	});

	// A -> B represents  "A supertypeOf B"
	var Generalization = CustomLink.define('ecore.Generalization', {
		attrs: {
			line: {
				strokeWidth: 1,
				sourceMarker: {
					"type": "path",
					"d": "M 10 -5 0 0 10 5 z",
					"fill": "white",
				}
			} }
	});

	var EReference = CustomLink.define('ecore.EReference');

	/**
	 * Link attributes to include in EReferences to achieve different styles
	 *
	 * Inclusion example: link.attr(containmentRefAttrs)
	 */

	var containmentRefAttrs = {
		line: {
			sourceMarker: {
				'type': 'path',
				'd': 'M 14 0 7 -4 0 0 7 4 14 0 z'
			}
		}
	};

	var unidirectionalRefAttrs = {
		line: {
			targetMarker: {
				'type': 'path',
				'd': 'M 10 -5 0 0 10 5 5 0 10 -5 Z'
			}
		}
	};

	/**
	 * Exported elements
	*/

	return {
		util: util,
		createClassDiagram: createClassDiagram,
		EClass: EClass,
		EReference: EReference,
		Generalization: Generalization,
		containmentRefAttrs: containmentRefAttrs,
		unidirectionalRefAttrs: unidirectionalRefAttrs,
		Documentation: Documentation,
		Constraint: Constraint,
		Critique: Critique
	}
}();
