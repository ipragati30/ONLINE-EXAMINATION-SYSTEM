import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

class User {
    private String username;
    private String password;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean validate(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    public void updateProfile(String newUsername, String newPassword) {
        this.username = newUsername;
        this.password = newPassword;
        System.out.println("Profile updated successfully.");
    }
    
    public String getUsername() {
        return this.username;
    }
}

class MCQ {
    private String question;
    private List<String> options;
    private int correctAnswer;
    private int userAnswer = -1;

    public MCQ(String question, List<String> options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public void displayQuestion() {
        System.out.println(question);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ": " + options.get(i));
        }
    }

    public void selectAnswer(int answer) {
        this.userAnswer = answer;
    }

    public boolean isCorrect() {
        return userAnswer == correctAnswer;
    }
}

class Exam {
    private List<MCQ> questions;
    private int score = 0;

    public Exam(List<MCQ> questions) {
        this.questions = questions;
    }

    public void startExam(Scanner scanner) {
        for (MCQ question : questions) {
            question.displayQuestion();
            System.out.print("Select your answer: ");
            int answer = scanner.nextInt();
            question.selectAnswer(answer);

            if (question.isCorrect()) {
                score++;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return questions.size();
    }
}

public class OnlineExaminationSystem {
    private static User loggedInUser;
    private static Timer timer;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = new User("student", "password123");

        // Login Functionality
        System.out.println("Welcome to the Online Examination System");
        while (true) {
            System.out.print("Enter Username: ");
            String username = scanner.next();
            System.out.print("Enter Password: ");
            String password = scanner.next();
            
            if (user.validate(username, password)) {
                loggedInUser = user;
                System.out.println("Login Successful!");
                break;
            } else {
                System.out.println("Invalid Credentials. Try Again.");
            }
        }

        // Update Profile Functionality
        System.out.println("Do you want to update your profile? (yes/no)");
        String updateProfile = scanner.next();
        if (updateProfile.equalsIgnoreCase("yes")) {
            System.out.print("Enter new Username: ");
            String newUsername = scanner.next();
            System.out.print("Enter new Password: ");
            String newPassword = scanner.next();
            loggedInUser.updateProfile(newUsername, newPassword);
        }

        // Start Exam
        List<MCQ> questions = new ArrayList<>();
        questions.add(new MCQ("What is 2 + 2?", Arrays.asList("3", "4", "5", "6"), 2));
        questions.add(new MCQ("What is the capital of France?", Arrays.asList("Berlin", "Madrid", "Paris", "Lisbon"), 3));
        Exam exam = new Exam(questions);

        System.out.println("Starting exam...");
        setExamTimer(exam, scanner, 1);  // Set timer to 1 minute
    }

    // Timer for the exam with Auto-submit
    public static void setExamTimer(Exam exam, Scanner scanner, int durationInMinutes) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Time's up! Auto-submitting exam...");
                submitExam(exam);
                logout();
            }
        }, durationInMinutes * 60 * 1000);  // Convert minutes to milliseconds

        // Start the exam manually
        exam.startExam(scanner);
        submitExam(exam);
        logout();
    }

    // Submit the exam and display the score
    public static void submitExam(Exam exam) {
        System.out.println("Exam submitted. You scored: " + exam.getScore() + " out of " + exam.getTotalQuestions());
        timer.cancel();  // Stop the timer
    }

    // Logout Functionality
    public static void logout() {
        System.out.println("You have been logged out.");
        System.exit(0);
    }
}


