package ro.a.tasksflow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTask extends AppCompatActivity {

    private EditText etTitle;
    private EditText etDesc;
    private Spinner spSub;
    private EditText etDeadline;
    private Spinner spPrior;
    private CheckBox ckComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.etTitle = findViewById(R.id.etTitle);
        this.etDesc = findViewById(R.id.etDescription);
        this.spSub = findViewById(R.id.spSub);
        this.etDeadline = findViewById(R.id.tvDeadline);
        this.spPrior = findViewById(R.id.spPrior);
        this.ckComplete = findViewById(R.id.ckComplete);

        Button addBtn = findViewById(R.id.btnAddNewTask);
        addBtn.setOnClickListener(
                v->{
                    save();
                }
        );
    }

    public void save()
    {
        String title = this.etTitle.getText().toString().trim();
        String description = this.etDesc.getText().toString().trim();
        String sub = this.spSub.getSelectedItem().toString();
        String deadline = this.etDeadline.getText().toString().trim();
        String prior = this.spPrior.getSelectedItem().toString();
        boolean complete = this.ckComplete.isChecked();

        if (title.isEmpty())
        {
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        if (deadline.isEmpty())
        {
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        Task t = new Task(
                title,
                (description.isEmpty() ? "" : description),
                sub,
                deadline,
                prior,
                complete
        );

        Intent data = new Intent();
        data.putExtra("task", t);
        setResult(RESULT_OK, data);
        finish();
    }
}