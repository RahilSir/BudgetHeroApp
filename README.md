BudgetApp is a simple, mobile-first Android application designed to help users track their expenses and manage budgets effectively.
It allows users to view, filter, and summarize expenses over a time period, and displays associated images for each recorded expense.

Features
View All Expenses: Lists all expenses with details (name, amount, date, times, description, category, image).

Filter by Date: Allows users to view expenses within a selected start and end date.

Category Summaries: Automatically calculates and displays total amounts spent per category.

Display Images: Loads and displays an image for each expense item (with permission).

Simple Navigation: Easy-to-use "Back" functionality to return to the previous screen.

Monthly Report: A feature to view all your monthly expenses with the amount of each expense

Report Download: The monthly report will be able to be downloaded as a PDF document

Visual Representation: A bar graph showing expenses with the amounts

BudgetApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── example/
│   │   │   │           └── budgetapp/
│   │   │   │               ├── data/
│   │   │   │               │   └── Expense.kt    // Model class for expenses
│   │   │   │               └── ExpenseListActivity.kt  // Activity to display expenses
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_expense_list_activity.xml
│   │   │   │   │   └── expense_item.xml
│   │   │   │   ├── drawable/
│   │   │   │   └── values/
│   │   │   │       └── strings.xml, colors.xml, styles.xml
│   │   ├── AndroidManifest.xml
├── build.gradle
└── README.md




Technologies Used
Kotlin - Primary development language

Android SDK - Android native components

Parcelable - For passing expense data between activities

Permissions API - For accessing device storage images

XML - For designing layouts

Permissions Required
xml
Copy
Edit
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
This permission is used to access and display images from device storage.

(For Android versions < 13, you might use READ_EXTERNAL_STORAGE instead.)


Getting Started
Clone the repository:

bash
Copy
Edit
git clone https://github.com/RahilSir/BudgetApp.git
Open the project in Android Studio.

Sync Gradle files.

Run the app on an Android emulator or device.

Grant permission to access device images when prompted.


Author
Rahil Sirkissoon

rahilsirkissoon@gmail.com

GitHub: @RahilSir
