package chartexport.exporters;

//import java.io.File;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
//import java.util.Set;
//import javafx.util.StringConverter;

//import javax.imageio.ImageIO;

import chartexport.utils.DateAxis310;
import chartexport.utils.IntegerStringConverter;
//import javafx.scene.Node;
import javafx.scene.Scene;
//import javafx.application.Application;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
//import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;
//import javafx.embed.swing.SwingFXUtils;
import datamodel.IElement;
import datamodel.SchemaHeartbeatElement;

/**
 * Creates a line chart with dates at X axis using Gerrit Grunwald's DateAxis310.
 * source: https://bitbucket.org/hansolo/dateaxis310/src/master/
 */

public class DateLineChartExporter extends AbstractLineChartExporter<LocalDateTime> {// extends Application{

	public DateLineChartExporter(String pOutputPath, String pTitle, HashMap<Integer, ArrayList<IElement>> inputTupleCollection, 
			String pXAttribute, ArrayList<String> pYAttributes, HashMap<String, Integer> pAttributePositions, Stage primaryStage) {
		super(pOutputPath, pTitle, inputTupleCollection, pXAttribute, pYAttributes, pAttributePositions, primaryStage);
	}//end constructor

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		createSeries();
		if (this.allSeries.size() == 0)
			return;

		//double xTickUnit = Math.round((double) (maxX - minX)/10.0);
		//double yTickUnit = (double) (maxY - minY)/10.0;
		//final NumberAxis xAxis = new NumberAxis(xAttribute, minX - xTickUnit, maxX + xTickUnit, xTickUnit);
		//final NumberAxis yAxis = new NumberAxis(yAttribute, minY - yTickUnit, maxY + yTickUnit, yTickUnit);

		//EITHER the above, or the below
		// TODO: change NumberAxis to show integer values & maybe manual scaling
		final DateAxis310 xAxis = new DateAxis310();
		final NumberAxis yAxis = new NumberAxis();		
		xAxis.setAutoRanging(true);
		yAxis.setAutoRanging(true);

		xAxis.setLabel(xAttribute);
		//LocalDateTime dt = new LocalDateTime();
		//xAxis.setTickLabelFormatter(arg0);;;
		String yAxisLabel = yAttributes.get(0);
		for (int i=1; i<yAttributes.size();i++) {
			yAxisLabel += " & " + yAttributes.get(i);
		}
		yAxis.setLabel(yAxisLabel);
		//xAxis.setMinorTickVisible(false);
		yAxis.setMinorTickVisible(false);
		yAxis.setTickLabelFormatter(new IntegerStringConverter());

		this.lineChart = new LineChart<LocalDateTime,Number>(xAxis,yAxis);
		
		for(XYChart.Series<LocalDateTime,Number> nextSeries: this.allSeries)
			this.lineChart.getData().add(nextSeries);

		this.lineChart.setTitle(chartTitle);
		this.lineChart.setHorizontalGridLinesVisible(false);
		this.lineChart.setVerticalGridLinesVisible(false);
		
		xAxis.tickLabelFontProperty().set(Font.font(16));
		yAxis.tickLabelFontProperty().set(Font.font(16));
		
		//necessary, or axis tick values do not show in dump
		xAxis.setAnimated(false);
		yAxis.setAnimated(false);
		xAxis.setTickLabelRotation(-90);

		Scene scene  = new Scene(this.lineChart, 500, 500);
		stage.setScene(scene);

		//switch the flag to true if you want a report of the children of the lineChart and their styles
		Boolean reportChildrenFlag = false;
		if (reportChildrenFlag)
			reportChildrenOfChart();
		
		this.lineChart.setLegendVisible(false);
		
		// TODO: find how to add resources to jar and load css files from there
		//File f = new File("resources/stylesheets/lineChartStyle.css");
		//scene.getStylesheets().clear();
		//this.lineChart.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		this.lineChart.getStylesheets().add(getClass().getResource("stylesheets/lineChartStyle.css").toExternalForm());
		
		//Export to png
		saveAsPng(scene, outputPath);
				
		//INVERT COMMENT STATUS THIS OUT ONCE DONE!
		//stage.show();
		stage.close();
		
	}//end lineCreation
	
	@Override
	public void createSeries() {
		this.allSeries = new ArrayList<XYChart.Series<LocalDateTime,Number>>();
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		
		for(int i=0; i<yAttributes.size();i++ ) {
			XYChart.Series<LocalDateTime,Number> newSeries = new XYChart.Series<LocalDateTime,Number>();
			newSeries.setName(yAttributes.get(i));
			for (IElement tuple: inputTupleCollection.get(0)) {
					String xValue = tuple.getStringValueByPosition(xAttributePos);
					LocalDateTime xDate = LocalDateTime.parse(xValue, dateFormatter);
					Integer yValue = tuple.getIntValueByPosition(yAttributePoss.get(i));
					if(!(xValue.equals(IElement._ERROR_STRING)) && (yValue != IElement._ERROR_CODE)) {
						newSeries.getData().add(new XYChart.Data<LocalDateTime,Number>(xDate, yValue));
					}
					//System.out.println("x:" + xValue + "\ty:" + yValue);
			}
			this.allSeries.add(newSeries);
		}
		//return this.allSeries;
	}


}//end class
