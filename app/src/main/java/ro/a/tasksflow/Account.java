package ro.a.tasksflow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        User u = getIntent().getSerializableExtra("user", User.class);

        TextView tvUser = findViewById(R.id.tvUser);
        TextView tvMail = findViewById(R.id.tvMail);
        TextView tvTasks = findViewById(R.id.tvTasks);

        tvUser.setText(u.getUsername());
        tvMail.setText(u.getMail());
        tvTasks.setText("Unfinised tasks: " + Math.toIntExact(u.getTasks().stream().filter(t -> !t.isCompleted()).count()));

        Button btnContact = findViewById(R.id.btnSupport);
        btnContact.setOnClickListener(
                v -> {
                    Intent send = new Intent(Intent.ACTION_SEND, Uri.parse("contact@taskmanager.com"));
                    send.setType("test/plain");

                    startActivity(send);
                }
        );
    }
}