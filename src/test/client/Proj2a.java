package test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.core.client.JsonUtils;
import java.util.ArrayList;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Proj2a implements EntryPoint, ClickHandler
{
   VerticalPanel mainPanel = new VerticalPanel();
   String baseURL = "http://localhost:3000";
   ArrayList<MyStudent> students =
      new ArrayList<MyStudent>();
   JsArray<Student> jsonData;
   Button addStudentButton = new Button("Add Student");
   TextBox fnBox = new TextBox();
   TextBox lnBox = new TextBox();
   TextBox majBox = new TextBox();
   
   private static class MyStudent
   {
      private int id;
      private String first_name;
      private String last_name;
      private String major;
      
      public MyStudent(int id, String fn, String ln, String maj)
      {
         this.id = id;
         this.first_name = fn;
         this.last_name = ln;
         this.major = maj;
      }
   }
   public void onModuleLoad()
   {
      //String url = baseURL + "/students/index.json";
      //getRequest(url,"getStudents");
	  addStudentButton.addClickHandler(this); 
      RootPanel.get().add(mainPanel);
      setupAddStudent();
   }
   public void onClick(ClickEvent e)
   {
	   Object source = e.getSource();
	   if (source == addStudentButton) {
		   String url = baseURL + "/students/createStudent";
		   String postData = URL.encode("first_name") + "=" +
			  URL.encode(fnBox.getText().trim()) + "&" +
			  URL.encode("last_name") + "=" +
			  URL.encode(lnBox.getText().trim()) + "&" +
			  URL.encode("major") + "=" +
			  URL.encode(majBox.getText().trim());
		   postRequest(url,postData,"postStudent");
	   }
   }
   public void getRequest(String url, final String getType) {
      final RequestBuilder rb = new
         RequestBuilder(RequestBuilder.GET,url);
      try {
         rb.sendRequest(null, new RequestCallback()
         {
            public void onError(final Request request,
               final Throwable exception)
            {
               Window.alert(exception.getMessage());
            }
            public void onResponseReceived(final Request request,
               final Response response)
            {
               if (getType.equals("getStudents")) {
                  showStudents(response.getText());
               }
            }
         });
      }
      catch (final Exception e) {
         Window.alert(e.getMessage());
      }
   } // end getRequest()
   public void postRequest(String url, String data, final String postType)
   {
	   final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,url);
	   rb.setHeader("Content-type","application/x-www-form-urlencoded");
	   try {
		   rb.sendRequest(data, new RequestCallback()
		   {
			   public void onError(final Request request, final Throwable exception)
			   {
				   Window.alert(exception.getMessage());
			   }
			   public void onResponseReceived(final Request request, final Response response)
			   {
				   if (postType.equals("postStudent")) {
					   mainPanel.clear();
					   String url = baseURL + "/students/index.json";
					   getRequest(url,"getStudents");
				   }
			   }
		   });
	   }
	   catch (Exception e) {
		   Window.alert(e.getMessage());
	   }
   } // end postRequest()
   private void showStudents(String responseText)
   {
      jsonData = getData(responseText);
      Student student = null;
      for (int i = 0; i < jsonData.length(); i++) {
         student = jsonData.get(i);
         students.add(new MyStudent(student.getID(),
            student.getFirstName(), student.getLastName(),
            student.getMajor()));
      }
      CellTable<MyStudent> table = new CellTable<MyStudent>();
      TextColumn<MyStudent> fnameCol = 
         new TextColumn<MyStudent>()
         {
            @Override
            public String getValue(MyStudent student)
            {
               return student.first_name;
            }
         };
      TextColumn<MyStudent> lnameCol = 
         new TextColumn<MyStudent>()
         {
            @Override
            public String getValue(MyStudent student)
            {
               return student.last_name;
            }
         };
      
      table.addColumn(fnameCol, "First Name");
      table.addColumn(lnameCol, "Last Name");
      table.setRowCount(students.size(),true);
      table.setRowData(0,students);
      mainPanel.add(table);
   } // end showStudents()
   private void setupAddStudent()
   {
	   VerticalPanel addStudentPanel = new VerticalPanel();
	   Label fnLabel = new Label("First Name");
	   HorizontalPanel fnRow = new HorizontalPanel();
	   fnRow.add(fnLabel);
	   fnRow.add(fnBox);
	   addStudentPanel.add(fnRow);
	   Label lnLabel = new Label("Last Name");
	   HorizontalPanel lnRow = new HorizontalPanel();
	   lnRow.add(lnLabel);
	   lnRow.add(lnBox);
	   addStudentPanel.add(lnRow);
	   Label majLabel = new Label("Major");
	   HorizontalPanel majRow = new HorizontalPanel();
	   majRow.add(majLabel);
	   majRow.add(majBox);
	   addStudentPanel.add(majRow);
	   addStudentPanel.add(addStudentButton);
	   mainPanel.add(addStudentPanel);  
   }
   private JsArray<Student> getData(String json)
   {
      return JsonUtils.safeEval(json);
   }
}