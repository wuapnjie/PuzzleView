package com.xiaopo.flying.poiphoto.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiaopo.flying.anotherlayout.R;
import com.xiaopo.flying.poiphoto.Configure;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.PhotoManager;
import com.xiaopo.flying.poiphoto.datatype.Album;
import com.xiaopo.flying.poiphoto.ui.adapter.AlbumAdapter;
import com.xiaopo.flying.poiphoto.ui.custom.DividerLine;
import java.util.List;

/**
 * the fragment to display album
 *
 * @author wupanjie
 */
public class AlbumFragment extends Fragment {

  RecyclerView mAlbumList;

  private AlbumAdapter mAlbumAdapter;
  private PhotoManager mPhotoManager;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.poiphoto_fragment_album, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    init(view);

    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      this.requestPermissions(
          new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE },
          Define.DEFAULT_REQUEST_PERMISSION_CODE);
    } else {
      startLoad();
    }
  }
  //
  //    @Override
  //    public void onResume() {
  //        super.onResume();
  //        new AlbumTask().execute();
  //    }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == Define.DEFAULT_REQUEST_PERMISSION_CODE
        && grantResults[0] == PackageManager.PERMISSION_GRANTED
        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
      startLoad();
    }
  }

  private void startLoad() {
    new AlbumTask().execute();
  }

  private void init(View view) {
    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    if (toolbar != null) {
      initToolbar(toolbar);
    }

    mAlbumList = (RecyclerView) view.findViewById(R.id.album_list);

    mPhotoManager = new PhotoManager(getContext());

    mAlbumList.setLayoutManager(new LinearLayoutManager(getContext()));

    mAlbumAdapter = new AlbumAdapter();
    mAlbumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(View v, int position) {
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .addToBackStack("Album")
            .replace(R.id.container, PhotoFragment.newInstance(mAlbumAdapter.getBuckedId(position)))
            .commit();
      }
    });

    mAlbumList.setAdapter(mAlbumAdapter);
    mAlbumList.addItemDecoration(new DividerLine());
  }

  private void initToolbar(Toolbar toolbar) {
    Configure configure = ((com.xiaopo.flying.poiphoto.ui.PickActivity) getActivity()).getConfigure();
    toolbar.setTitleTextColor(configure.getToolbarTitleColor());
    toolbar.setTitle(configure.getAlbumTitle());
    toolbar.setBackgroundColor(configure.getToolbarColor());
  }

  private void refreshAlbumList(List<Album> alba) {
    mAlbumAdapter.refreshData(alba);
  }

  private class AlbumTask extends AsyncTask<Void, Integer, List<Album>> {

    @Override
    protected List<Album> doInBackground(Void... params) {
      return mPhotoManager.getAlbum();
    }

    @Override
    protected void onPostExecute(List<Album> alba) {
      super.onPostExecute(alba);
      refreshAlbumList(alba);
    }
  }
}
