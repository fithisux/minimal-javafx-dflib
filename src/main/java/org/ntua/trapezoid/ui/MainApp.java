package org.ntua.trapezoid.ui;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.dflib.DataFrame;
import static org.dflib.Exp.$col;
import org.dflib.echarts.ECharts;
import org.dflib.echarts.EChartHtml;
import org.dflib.echarts.SeriesOpts;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
public class MainApp {

	@FXML
	WebView dflibWebView;
	
	WebEngine dflibWebEngine;
	
	
	public void loadCharts() {
		System.out.println("loading");
		
		System.out.println("loaded");
	}

	/**
	 * Initializes the root layout.
	 */
	@FXML
	public void initialize() {
		System.out.println("Initialized1");
		
		dflibWebEngine = this.dflibWebView.getEngine();
		
		dflibWebEngine.getLoadWorker().stateProperty()
		.addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov,
					State oldState, State newState) {
				System.out.println("heatmap "+newState);
				if (newState == State.SUCCEEDED) {
					loadDFLIB();
				}
			}
		});
		dflibWebEngine.load(getClass().getClassLoader().getResource("newcanvas.html").toExternalForm());
		
		System.out.println("Initialized2");
	}


	private void loadDFLIB() {
		
		System.out.println("load-dflib");

		DataFrame df = DataFrame.foldByRow("name", "salary").of(
						"J. Cosin", 120000,
						"J. Walewski", 80000,
						"J. O'Hara", 95000)
				.sort($col("salary").desc());

		EChartHtml chart = ECharts
				.chart()
				.xAxis("name")
				.series(SeriesOpts.ofBar(), "salary")
				.plot(df);
		
        drawChart(dflibWebEngine, chart);
	}
	
	
	private Node toScriptNode(String xmlString) {
		var prelude = "<script type='text/javascript'>";
		var preludeModified = "<script type='text/javascript'>\n//<![CDATA[\n";
		var epilogue = "</script>";
		var epilogueModified = "\n//]]>\n</script>";
		
		var modifiedXMLString = xmlString.replace(prelude, preludeModified).replace(epilogue, epilogueModified).replace("null,", "null, {renderer: 'svg'}");
	    try {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(modifiedXMLString));
		    Document xmlDoc = null;
	        xmlDoc = builder.parse(is);
	        return xmlDoc.getFirstChild();
	    } catch (SAXException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    } catch (ParserConfigurationException e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	private Node toDivNode(String xmlString) {
	    try {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(xmlString));
		    Document xmlDoc = null;
	        xmlDoc = builder.parse(is);
	        return xmlDoc.getFirstChild();
	    } catch (SAXException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    } catch (ParserConfigurationException e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	
	private void drawChart(WebEngine engine, EChartHtml chart) {
		
		var scriptNode = toScriptNode(chart.getScript());
		var divNode = toDivNode(chart.getContainer());
		var divNodeAttributes = divNode.getAttributes();
		
		String idValue = null;
		String sizeValue = null;
		
		for(int i=0;i<divNodeAttributes.getLength();i++) {
			var someNode = divNodeAttributes.item(i);
			
			if(someNode.getNodeName() == "id") {
				idValue = someNode.getTextContent();
			} else if(someNode.getNodeName() == "style") {
				sizeValue = someNode.getTextContent();
			}
		}
		
		var doc = engine.getDocument();
		
		var element = doc.createElement("div");
		element.setAttribute("id", idValue);
		element.setAttribute("style", sizeValue);
		
		System.out.println("append");
		doc.getElementById("thebody").appendChild(element);
		System.out.println("appended");
		System.out.println(scriptNode.getTextContent());
        engine.executeScript(scriptNode.getTextContent());
        System.out.println("scripted");
		
	}
}