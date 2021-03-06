package tech.liujin.androidmodellib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import tech.liujin.androidmodellib.test.BitmapFragment;
import tech.liujin.androidmodellib.test.DownLoadFragment;
import tech.liujin.androidmodellib.test.FileDiskFragment;
import tech.liujin.androidmodellib.test.FileFragment;
import tech.liujin.androidmodellib.test.JsonFragment;
import tech.liujin.androidmodellib.test.MemoryBitmapFragment;
import tech.liujin.androidmodellib.test.MemoryMapFragment;
import tech.liujin.androidmodellib.test.OkHttpFragment;
import tech.liujin.androidmodellib.test.RetrofitFragment;
import tech.liujin.androidmodellib.test.StreamFragment;
import tech.liujin.androidmodellib.util.FileManager;
import tech.liujin.androidmodellib.util.ScreenSize;

/**
 * @author liujin
 */
public class MainActivity extends AppCompatActivity {

      private FrameLayout    mContainer;
      private NavigationView mNavigationView;
      private DrawerLayout   mDrawer;

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_main );
            ScreenSize.init( this );
            FileManager.init( this );
            initView();
      }

      private void initView ( ) {

            mDrawer = findViewById( R.id.drawer );
            mNavigationView = findViewById( R.id.navigationView );
            mContainer = findViewById( R.id.container );

            mNavigationView.setNavigationItemSelectedListener( new MainMenuClickListener() );
      }

      private class MainMenuClickListener implements OnNavigationItemSelectedListener {

            @Override
            public boolean onNavigationItemSelected ( @NonNull MenuItem item ) {

                  switch( item.getItemId() ) {
                        case R.id.menu00:
                              changeFragment( MemoryMapFragment.newInstance() );
                              break;
                        case R.id.menu01:
                              changeFragment( MemoryBitmapFragment.newInstance() );
                              break;
                        case R.id.menu02:
                              changeFragment( FileFragment.newInstance() );
                              break;
                        case R.id.menu03:
                              changeFragment( FileDiskFragment.newInstance() );
                              break;
                        case R.id.menu04:
                              changeFragment( OkHttpFragment.newInstance() );
                              break;
                        case R.id.menu05:
                              changeFragment( RetrofitFragment.newInstance() );
                              break;
                        case R.id.menu06:
                              changeFragment( DownLoadFragment.newInstance() );
                              break;
                        case R.id.menu07:
                              changeFragment( BitmapFragment.newInstance() );
                              break;
                        case R.id.menu08:
                              changeFragment( JsonFragment.newInstance() );
                              break;
                        case R.id.menu09:
                              changeFragment( StreamFragment.newInstance() );
                              break;
                        default:
                              break;
                  }

                  mDrawer.closeDrawer( Gravity.START );
                  return true;
            }
      }

      private void changeFragment ( Fragment fragment ) {

            getSupportFragmentManager().beginTransaction()
                                       .replace( R.id.container, fragment )
                                       .commit();
      }
}

