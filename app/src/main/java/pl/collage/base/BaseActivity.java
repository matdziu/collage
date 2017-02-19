package pl.collage.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.collage.R;
import pl.collage.changepassword.ChangePasswordActivity;
import pl.collage.login.LoginActivity;
import pl.collage.util.interactors.FirebaseAuthInteractor;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;

    private SystemBarTintManager tintManager;
    private BasePresenter basePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);

        basePresenter = new BasePresenter(new FirebaseAuthInteractor());

        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#20000000"));

        baseToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setSupportActionBar(baseToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                basePresenter.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case R.id.menu_change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract int getLayoutResource();

    public void hideHomeNavigation() {
        baseToolbar.setVisibility(View.GONE);
        tintManager.setTintColor(Color.parseColor("#00000000"));
    }

    public void showHomeNavigation() {
        baseToolbar.setVisibility(View.VISIBLE);
        tintManager.setTintColor(Color.parseColor("#20000000"));
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
