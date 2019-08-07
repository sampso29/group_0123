import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

// One HR Person hires for their own section of the company
public class HR_Coordinator extends User {

    private Company company;

    HR_Coordinator(String Username, String Password) {
        super(Username, Password);
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    void HRGUISetUp(Stage stage, User loggedUser, SystemAdmin systemAdmin, Scene loginPage) {
        if (loggedUser.getClass() == HR_Coordinator.class) {
            Group HRPortalScene = new Group();
            Scene HRBasePage = new Scene(HRPortalScene, 450, 250);
            stage.setScene(HRBasePage);

            String welcomeMessage = "Welcome to the Human Resources page, " + loggedUser.getUsername();
            Label welcomeLabel = new Label(welcomeMessage);
            Label actions = new Label("What do you want to do? Please select an option below:");
            Button addJobs = new Button("Add a job");
            Button viewClosedJobs = new Button("Manage closed jobs"); // for a specific job, view the applicants
            Button viewAllApps = new Button("View all applicants"); // for a specific applicant, view the jobs they applied for
            Button logout = new Button("Logout");
            GridPane messageGrid = new GridPane();
            GridPane buttonGrid = new GridPane();

            messageGrid.add(welcomeLabel, 1, 0);
            messageGrid.add(actions, 1, 2);
            buttonGrid.add(addJobs, 1, 0);
            buttonGrid.add(viewClosedJobs, 2, 0);
            buttonGrid.add(viewAllApps, 1, 2);
            buttonGrid.add(logout, 2, 2);

            BorderPane HRPlacement = new BorderPane();
            messageGrid.setHgap(20);
            messageGrid.setVgap(5);
            buttonGrid.setHgap(20);
            buttonGrid.setVgap(5);
            HRPlacement.setTop(messageGrid);
            HRPlacement.setBottom(buttonGrid);

            HRPortalScene.getChildren().addAll(HRPlacement);

            logout.setOnAction((ActionEvent ex) -> stage.setScene(loginPage));

            //Job Creation page -- where we create job postings
            addJobs.setOnAction((ActionEvent addJob) -> {
                Group createJobs = new Group();
                Scene createJobsPage = new Scene(createJobs, 725, 600);
                stage.setScene(createJobsPage);
                //-------------
                //Date closingDate = new Date(int year,int month,int day);
                DatePicker datePicker = new DatePicker();
                Label closingMessage = new Label("Closing Date:");
                Label positionLabel = new Label("What position are we creating?");
                Label companyLabel = new Label("What company is this for?");
                Label availJobsLabel = new Label("How many people are we hiring?");
                Label intStageLabel = new Label("Please add a stage to the interview process:");
                Label viewInterviewerLabel = new Label("Select Interviewers to interview people for this job:"); // view all the stages that have been added
                TextField positionField = new TextField();
                TextField companyField = new TextField();
                TextField availJobField = new TextField();
                TextField intStageField = new TextField();
                intStageField.setPromptText("i.e. Phone Interview");
                Button addStageToProcess = new Button("Add Stage");
                Button addInterviewer = new Button("Add Interviewer");
                Button createNewPost = new Button("Create Job");
                Button returnAddJ = new Button("Back");
                RadioButton fullTime = new RadioButton("fullTime");
                RadioButton partTime = new RadioButton("partTime");
                GridPane cMessageGrid = new GridPane();
                GridPane dateGrid = new GridPane();
                GridPane positionGrid = new GridPane();
                ToggleGroup radioSet = new ToggleGroup();
                fullTime.setToggleGroup(radioSet);
                partTime.setToggleGroup(radioSet);


                ObservableList<String> listStages = FXCollections.observableArrayList();
                ObservableList<String> decidedInterviewers = FXCollections.observableArrayList(); // for ViewList
                ComboBox<String> interviewerDropdown = new ComboBox<>(); // not populating bc getListInterviewers() method in UserAccess not working

                //TODO make this accommodate for Company
                for (Interviewer interviewer : systemAdmin.getUserManager().getListInterviewers()) {
                    //listInterviewers.add(interviewer.getUsername());
                    interviewerDropdown.getItems().add(interviewer.getUsername());
                }

                interviewerDropdown.setMaxWidth(200.00);
                interviewerDropdown.setMinWidth(200.00);

                cMessageGrid.add(closingMessage, 1, 0);
                dateGrid.add(closingMessage, 1, 0);
                dateGrid.add(datePicker, 2, 0);
                positionGrid.add(positionLabel, 1, 0);
                positionGrid.add(positionField, 2, 0);
                positionGrid.add(companyLabel, 1, 2);
                positionGrid.add(companyField, 2, 2);
                positionGrid.add(availJobsLabel,1,4);
                positionGrid.add(availJobField,2,4);
                positionGrid.add(intStageLabel, 1, 6); //4
                positionGrid.add(intStageField, 2, 6); //4
                positionGrid.add(addStageToProcess, 3, 6); //4
                positionGrid.add(viewInterviewerLabel, 1, 10);//8
                positionGrid.add(interviewerDropdown, 2, 10); //8
                positionGrid.add(addInterviewer, 3, 10); //8
                positionGrid.add(createNewPost, 1, 12); //10
                positionGrid.add(returnAddJ, 1, 13); //12
                positionGrid.add(fullTime, 2, 12);
                positionGrid.add(partTime, 3, 12);

                cMessageGrid.setHgap(20);
                cMessageGrid.setVgap(5);
                dateGrid.setHgap(20);
                dateGrid.setVgap(5);
                positionGrid.setHgap(20);
                positionGrid.setVgap(5);

                BorderPane CreateJobPlacement = new BorderPane();

                CreateJobPlacement.setCenter(dateGrid);
                CreateJobPlacement.setBottom(positionGrid);

                createJobs.getChildren().addAll(CreateJobPlacement);

                //TODO: should we number the stages to show the order they are in?
                addStageToProcess.setOnAction((ActionEvent addStage) -> {
                    String strISF = intStageField.getText();
                    if (!isNullOrEmpty(strISF)) {
                        listStages.add(strISF);
                        ListView<String> showStagesList = new ListView<>();
                        showStagesList.setItems(listStages);
                        showStagesList.setPrefSize(100.00, 70.00);
                        positionGrid.add(showStagesList, 1, 8);
                        intStageField.clear();
                    }
                });

                addInterviewer.setOnAction((ActionEvent addIntToList) -> {
                    String interviewerUsername = interviewerDropdown.getValue();
                    if (!isNullOrEmpty(interviewerUsername)) {
                        boolean add = true;
                        for (String str : decidedInterviewers) {
                            if (str.equals(interviewerUsername)) {
                                add = false;
                            }
                        }
                        if (add) {
                            decidedInterviewers.add(interviewerUsername);
                            ListView<String> choiceInterviewers = new ListView<>();
                            choiceInterviewers.setItems(decidedInterviewers);
                            choiceInterviewers.setPrefSize(100.00, 70.00);
                            positionGrid.add(choiceInterviewers, 1, 11);
                        }
                    }
                });

                createNewPost.setOnAction((ActionEvent CreateJob) -> {
                    if (!listStages.isEmpty()) {
                        Date closeDate = Date.from(datePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                        String position = positionField.getText();
                        String company = companyField.getText();
                        //if (availJobField.getText().trim().matches("^/d*$")){
                        int numPositions = Integer.parseInt(availJobField.getText());
                        ArrayList<String> stagesOfInterview = new ArrayList<>();
                        for (String str : listStages) {
                            stagesOfInterview.add(str);
                        }

                        ArrayList<Interviewer> chosenInterviewers = new ArrayList<>();
                        for (String str : decidedInterviewers) {
                            chosenInterviewers.add((Interviewer) systemAdmin.getUserManager().getUser(str));
                        }

                        Job job = new Job(position, company, "tag", 0, stagesOfInterview);

                        if (radioSet.getSelectedToggle() == fullTime) {
                            job.setTag("fullTime");
                            systemAdmin.getJobManager().addJobPosting(job, closeDate, chosenInterviewers, numPositions);
                            stage.setScene(HRBasePage);

                        } else if (radioSet.getSelectedToggle() == partTime) {
                            job.setTag("partTime");
                            systemAdmin.getJobManager().addJobPosting(job, closeDate, chosenInterviewers, numPositions);
                            stage.setScene(HRBasePage);
                            //((JobPosting)(systemAdmin.getJobManager().getJobPosting(position))).getJob().setTag("partTime");
                        } else {
                            systemAdmin.getAlert("tag").showAndWait();
                        }
                    }else{
                        systemAdmin.getAlert("job").showAndWait();
                    }
                });
                returnAddJ.setOnAction((ActionEvent exitPage) -> stage.setScene(HRBasePage));
            });

            viewClosedJobs.setOnAction((ActionEvent viewJob) -> { // this the closed jobs
                Group HRViewJobs = new Group();
                Scene createJobsPage = new Scene(HRViewJobs, 600, 600);
                stage.setScene(createJobsPage);
                Label ChooseJob = new Label("Choose a job:");
                Label roundLabel = new Label();
                /*ComboBox dropdown = new ComboBox();
                for (JobPosting jobPosting : jobManager.ViewJobs()) {
                    dropdown.getItems().add(jobPosting.getPosition());
                }*/
                Button viewApps = new Button("See applicants");
                Button distributeApps = new Button("Distribute applicants");
                Button advanceRoundButton = new Button("Advance Round");
                Button viewHiresButton = new Button("viewHires");
                Button returnViewJ = new Button("Back");
                GridPane ViewJobsGrid = new GridPane();

                ListView<String> scrollListJobs = new ListView<>();
                ObservableList<String> listJobs = FXCollections.observableArrayList();
                scrollListJobs.setItems(listJobs);
                scrollListJobs.setPrefSize(160.00, 120.00);
                int j = -1;
                for (JobPosting jobPosting : systemAdmin.getJobManager().viewClosedJobs()) {  //was ViewJobs before
                    listJobs.add(jobPosting.getJob().getPosition());
                }
                ViewJobsGrid.add(scrollListJobs, 2, j + 1);
                ViewJobsGrid.add(ChooseJob, 1, 0);
                ViewJobsGrid.add(viewApps, 1, 1);  // 3,1
                ViewJobsGrid.add(distributeApps, 2, 1); // 3,2
                ViewJobsGrid.add(advanceRoundButton, 3, 1); // 3,3
                ViewJobsGrid.add(returnViewJ, 3, 2);

                ViewJobsGrid.setHgap(20);
                ViewJobsGrid.setVgap(5);
                BorderPane ViewJobsPlacement = new BorderPane();
                ViewJobsPlacement.setTop(ViewJobsGrid);
                HRViewJobs.getChildren().add(ViewJobsPlacement);

                returnViewJ.setOnAction((ActionEvent exitPage) -> stage.setScene(HRBasePage));

                viewApps.setOnAction((ActionEvent seeApps) -> {
                    int i = 0;
                    String choice = scrollListJobs.getSelectionModel().getSelectedItem();
                    String[] listOfApp = systemAdmin.getJobManager().getClosedJob(choice).viewAllApplicants().split(",");  //was .viewApplicants() before
                    if (listOfApp.length != 0 && !isNullOrEmpty(listOfApp[0])) {
                        ListView<String> scrollListApps = new ListView<>();
                        ObservableList<String> listApps = FXCollections.observableArrayList();
                        scrollListApps.setItems(listApps);
                        scrollListApps.setPrefSize(160.00, 120.00);
                        for (String app : listOfApp) {
                            listApps.add(app);
                        }
                        ViewJobsGrid.add(scrollListApps, 2, i + 1);

                        /*Button viewButton = new Button("VIEW INFO");
                        appViewer.add(viewButton, 3, i + 1);
                        appViewer.setHgap(20);
                        appViewer.setVgap(5);
                        ViewJobsPlacement.setBottom(appViewer);

                        viewButton.setOnAction((ActionEvent ViewAppDocs) -> {
                            // see the specific applicant's documents
                        });*/
                    }
                });
                //todo: line 286 has a nullPointer error
                //put a break-line and debug
                distributeApps.setOnAction((ActionEvent disApps) -> {
                    String choice = scrollListJobs.getSelectionModel().getSelectedItem();
                    JobPosting tempJob = systemAdmin.getJobManager().getClosedJob(choice);
                    if (!tempJob.getHiringProcessor().isDistributed()){
                        tempJob.getHiringProcessor().sendListToInterview(systemAdmin.getUserManager());
                    }
                    if (tempJob.getHiringProcessor().isFinalRound()){ViewJobsGrid.add(viewHiresButton, 1, 2);}
                });
                advanceRoundButton.setOnAction((ActionEvent advRound) -> {
                    String choice = scrollListJobs.getSelectionModel().getSelectedItem();
                    JobPosting job = systemAdmin.getJobManager().getClosedJob(choice);
                    ArrayList<Applicant> appsForNextRound = job.getHiringProcessor().getRecommendList();
                    if (!appsForNextRound.isEmpty()) {
                        job.getHiringProcessor().getListFromHR(appsForNextRound);
                    }
                    int round = job.getHiringProcessor().getRoundNum();
                    int totRounds = job.getHiringProcessor().numStages();
                    roundLabel.setText("Round: " + Integer.toString(round)+ " / " + Integer.toString(totRounds));
                    ViewJobsGrid.add(roundLabel, 3, 0);
                    if (job.getHiringProcessor().isFinalRound()){ViewJobsGrid.add(viewHiresButton, 1, 2);}
                });
                viewHiresButton.setOnAction((ActionEvent checkRound) -> {
                    String choice = scrollListJobs.getSelectionModel().getSelectedItem();
                    JobPosting job = systemAdmin.getJobManager().getClosedJob(choice);
                    ListView listHires = new ListView();
                    ObservableList<String> listObsHires = FXCollections.observableArrayList();
                    listHires.setItems(listObsHires);
                    listHires.setPrefSize(160.00, 120.00);
                    for (Applicant app : job.getHiringProcessor().getRecommendList()) {
                        listObsHires.add(app.getUsername());
                    }
                    ViewJobsGrid.add(listHires, 2, 3);
                });
            });
            viewAllApps.setOnAction((ActionEvent viewAllApplicants) -> {
                Group viewApps = new Group();
                Scene createViewAppsPage = new Scene(viewApps, 600, 600);
                stage.setScene(createViewAppsPage);
                ComboBox dropApp = new ComboBox();
                for (User user : systemAdmin.getUserManager().viewUsers()) {
                    if (user instanceof Applicant) {
                        dropApp.getItems().add(user.getUsername());
                    }
                }
                Label appInfo = new Label("");
                Button returnViewApp = new Button("Back");
                Button viewButton = new Button("View applicants");
                GridPane ViewAppsGrid = new GridPane();

                ViewAppsGrid.add(dropApp, 10, 0);
                ViewAppsGrid.add(viewButton, 10, 1);
                ViewAppsGrid.add(appInfo, 10, 2);
                ViewAppsGrid.add(returnViewApp, 10, 15);

                ViewAppsGrid.setHgap(20);
                ViewAppsGrid.setVgap(5);
                BorderPane ViewJobsPlacement = new BorderPane();
                ViewJobsPlacement.setTop(ViewAppsGrid);
                viewApps.getChildren().add(ViewJobsPlacement);

                returnViewApp.setOnAction((ActionEvent exitPage) -> stage.setScene(HRBasePage));

                viewButton.setOnAction((ActionEvent seeApps) -> appInfo.setText(((Applicant)
                        (systemAdmin.getUserManager().getUser((String) dropApp.getValue()))).getInfo()));
            });
        } else {
            stage.setScene(loginPage);
        }
    }

    public String toString() {
        return "{H," + this.getUsername() + "," + this.getPassword() + "}";
    }
}