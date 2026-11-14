package ro.a.tasksflow;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private User user;
    private ArrayList<Task> shownTasks = new ArrayList<>();
    private ArrayAdapter<Task> adapter;
    private ActivityResultLauncher<Intent> addTask;

    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout filter = findViewById(R.id.layFiltr);
        LinearLayout update = findViewById(R.id.layUpt);
        update.setVisibility(GONE);

        Task t1 = new Task(
                "Android Project",
                "",
                "Android",
                "20/11/2026",
                "HIGH",
                false
        );

        Task t2 = new Task(
                "SQL Lab",
                "",
                "Data Base",
                "18/11/2026",
                "MEDIUM",
                true
        );

        Task t3 = new Task(
                "Machine Learning",
                "",
                "Machine Learning",
                "5/11/2026",
                "LOW",
                false
        );

        ArrayList<Task> userTasks = new ArrayList<>();
        userTasks.add(t1);
        userTasks.add(t2);
        userTasks.add(t3);

        this.user = new User(
                1,
                "Gigel",
                "gigel@gmail.com",
                userTasks
        );

        this.shownTasks = (ArrayList<Task>) this.user.getTasks().clone();

        TextView tvGreet = findViewById(R.id.tvGreetUser);
        tvGreet.setText(getString(R.string.greet, user.getUsername()));

        ListView lsTasks = findViewById(R.id.lsTasks);
        this.adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                this.shownTasks
        );
        lsTasks.setAdapter(adapter);

        lsTasks.setOnItemClickListener(
                (parent, view, position, id) -> {
                    if(update.getVisibility() == GONE || this.position != position)
                    {
                        this.position = position;

                        update.setVisibility(VISIBLE);
                        filter.setVisibility(GONE);

                        EditText etTitle = findViewById(R.id.etTitle);
                        EditText etDesc = findViewById(R.id.etDescription);
                        Spinner spSub = findViewById(R.id.spSub);
                        EditText etDeadline = findViewById(R.id.tvDeadline);
                        Spinner spPrior = findViewById(R.id.spPrior);
                        CheckBox ckComplete = findViewById(R.id.ckComplete);

                        Task t = this.shownTasks.get(position);
                        etTitle.setText(t.getTitle());
                        etDesc.setText(t.getDescription());

                        switch (t.getSubject()) {
                            case "Android":
                                spSub.setSelection(0);
                                break;
                            case "Data base":
                                spSub.setSelection(1);
                                break;
                            case "Machine Learning":
                                spSub.setSelection(2);
                                break;
                            case "Python":
                                spSub.setSelection(3);
                                break;
                        }

                        etDeadline.setText(t.getDeadline());

                        switch (t.getPriority()) {
                            case "low":
                                spPrior.setSelection(0);
                                break;
                            case "medium":
                                spPrior.setSelection(1);
                                break;
                            case "high":
                                spPrior.setSelection(2);
                                break;
                        }

                        ckComplete.setChecked(t.isCompleted());

                        Button updBtn = findViewById(R.id.btnUpdateTask);
                        updBtn.setOnClickListener(
                                v -> {
                                    Task newTask = update(etTitle, etDesc, spSub, etDeadline, spPrior, ckComplete, t);
                                    this.shownTasks.get(position).setTitle(newTask.getTitle());
                                    this.shownTasks.get(position).setDescription(newTask.getDescription());
                                    this.shownTasks.get(position).setSubject(newTask.getSubject());
                                    this.shownTasks.get(position).setDeadline(newTask.getDeadline());
                                    this.shownTasks.get(position).setPriority(newTask.getPriority());
                                    this.shownTasks.get(position).setCompleted(newTask.isCompleted());

                                    this.adapter.notifyDataSetChanged();

                                    update.setVisibility(GONE);
                                    filter.setVisibility(VISIBLE);
                                }
                        );
                    }
                    else
                    {
                        update.setVisibility(GONE);
                        filter.setVisibility(VISIBLE);
                    }
                }
        );

        lsTasks.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Delete Task")
                            .setMessage("Do you want to delete this task?")
                            .setPositiveButton(
                                    "Yes",
                                    (dialog, which) -> {
                                        Task t = this.shownTasks.get(position);

                                        this.user.getTasks().remove(t);
                                        this.shownTasks.remove(t);

                                        this.adapter.notifyDataSetChanged();
                                    })
                            .setNegativeButton("No", null).show();

                    return true;
                }
        );

        Button btnAdd = findViewById(R.id.btnAddTask);
        this.addTask = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null)
                    {
                        Task t = result.getData().getSerializableExtra("task", Task.class);

                        if (t != null)
                        {
                            this.user.addTask(t);

                            this.shownTasks.clear();
                            this.shownTasks.addAll(this.user.getTasks());

                            this.adapter.notifyDataSetChanged();
                        }
                    }
                }
        );
        btnAdd.setOnClickListener(
                v -> this.addTask.launch(new Intent(this, AddTask.class))
        );

        Spinner spFiltr = findViewById(R.id.spFiltr);
        spFiltr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String criteria = parent.getItemAtPosition(position).toString();
                filterTasks(criteria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nu facem nimic
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnItmAcc)
        {
            Intent sendData = new Intent(this, Account.class);
            sendData.putExtra("user", this.user);

            startActivity(sendData);
        }
        return true;
    }

    public Task update(EditText etTitle, EditText etDesc, Spinner spSub, EditText etDeadline, Spinner spPrior, CheckBox ckComplete, Task currentTask)
    {
        String title = etTitle.getText().toString().trim();
        String description = etDesc.getText().toString().trim();
        String sub = spSub.getSelectedItem().toString();
        String deadline = etDeadline.getText().toString().trim();
        String prior = spPrior.getSelectedItem().toString();
        boolean complete = ckComplete.isChecked();

        if (title.isEmpty())
        {
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();
            return currentTask;
        }

        if (deadline.isEmpty())
        {
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();
            return currentTask;
        }

        return new Task(
            title,
            (description.isEmpty() ? "" : description),
            sub,
            deadline,
            prior,
            complete
        );
    }

    public void filterTasks(String criteria)
    {
        this.shownTasks.clear();

        switch (criteria){
            case "All":
                this.shownTasks.addAll(this.user.getTasks());
                break;
            case "Only high priority tasks":
                this.shownTasks.addAll(this.user.getTasks().stream().filter(t -> t.getPriority().equals("high")).collect(Collectors.toList()));
                break;
        }

        this.adapter.notifyDataSetChanged();
    }
}