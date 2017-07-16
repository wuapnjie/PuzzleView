package com.xiaopo.flying.poiphoto.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.xiaopo.flying.anotherlayout.R;
import com.xiaopo.flying.poiphoto.Configure;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.PhotoManager;
import com.xiaopo.flying.poiphoto.datatype.Photo;
import com.xiaopo.flying.poiphoto.ui.adapter.PhotoAdapter;
import java.util.List;

/**
 * the fragment to display photo
 *
 * @author wupanjie
 */
public class PhotoFragment extends Fragment {

  private static final String TAG = PhotoFragment.class.getSimpleName();
  RecyclerView mPhotoList;

  private PhotoAdapter mAdapter;
  private PhotoManager mPhotoManager;

  public static PhotoFragment newInstance(String bucketId) {
    Bundle bundle = new Bundle();
    bundle.putString("bucketId", bucketId);
    PhotoFragment fragment = new PhotoFragment();
    fragment.setArguments(bundle);

    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPhotoManager = new PhotoManager(getContext());
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.poiphoto_fragment_photo, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    init(view);

    String buckedId = getArguments().getString("bucketId");
    new PhotoTask().execute(buckedId);
  }

  private void init(final View view) {
    final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    final Configure configure = ((com.xiaopo.flying.poiphoto.ui.PickActivity) getActivity()).getConfigure();
    if (toolbar != null) {
      initToolbar(toolbar, configure);
    }

    mPhotoList = (RecyclerView) view.findViewById(R.id.photo_list);

    mPhotoList.setLayoutManager(new GridLayoutManager(getContext(), 3));

    mAdapter = new PhotoAdapter();

    mAdapter.setMaxCount(configure.getMaxCount());

    mAdapter.setOnSelectedMaxListener(new PhotoAdapter.OnSelectedMaxListener() {
      @Override
      public void onSelectedMax() {
        Snackbar.make(view, configure.getMaxNotice(), Snackbar.LENGTH_SHORT).show();
      }
    });

    mAdapter.setOnPhotoSelectedListener(new PhotoAdapter.OnPhotoSelectedListener() {
      @Override
      public void onPhotoSelected(Photo photo, int position) {

      }
    });

    mAdapter.setOnPhotoUnSelectedListener(new PhotoAdapter.OnPhotoUnSelectedListener() {
      @Override
      public void onPhotoUnSelected(Photo photo, int position) {

      }
    });

    mPhotoList.setHasFixedSize(true);
    mPhotoList.setAdapter(mAdapter);
  }

  private void initToolbar(Toolbar toolbar, Configure configure) {

    if (configure != null) {

      toolbar.setTitle(configure.getPhotoTitle());
      toolbar.setBackgroundColor(configure.getToolbarColor());
      toolbar.setTitleTextColor(configure.getToolbarTitleColor());

      toolbar.inflateMenu(R.menu.menu_pick);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
          if (item.getItemId() == R.id.action_done) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(Define.PATHS, mAdapter.getSelectedPhotoPaths());
            intent.putParcelableArrayListExtra(Define.PHOTOS, mAdapter.getSelectedPhotos());
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
            return true;
          }
          return false;
        }
      });

      toolbar.setNavigationIcon(configure.getNavIcon());
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          getActivity().onBackPressed();
        }
      });
    }
  }

  private void refreshPhotoList(List<Photo> photos) {
    mAdapter.refreshData(photos);
  }

  private class PhotoTask extends AsyncTask<String, Integer, List<Photo>> {

    @Override
    protected List<Photo> doInBackground(String... params) {
      return mPhotoManager.getPhoto(params[0]);
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
      super.onPostExecute(photos);
      refreshPhotoList(photos);
    }
  }
}
