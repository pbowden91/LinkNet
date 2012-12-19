
package com.example.filenet.utils;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.FileObserver;
import android.support.v4.content.AsyncTaskLoader;


/**
 * Loader that returns a list of Files in a given file path.
 * 
 * @version 2012-10-28
 * 
 * @author paulburke (ipaulpro)
 * 
 */
public class FileLoader extends AsyncTaskLoader<List<File>> {

	private static final int FILE_OBSERVER_MASK = FileObserver.CREATE
			| FileObserver.DELETE | FileObserver.DELETE_SELF
			| FileObserver.MOVED_FROM | FileObserver.MOVED_TO
			| FileObserver.MODIFY | FileObserver.MOVE_SELF;
	
	private FileObserver mFileObserver;
	
	private List<File> mData;
	private String mPath;

	public FileLoader(Context context, String path) {
		super(context);
		this.mPath = path;
	}

	@Override
	public List<File> loadInBackground() {
		return FileUtils.getFileList(mPath);
	}

	@Override
	public void deliverResult(List<File> data) {
		if (isReset()) {
			onReleaseResources(data);
			return;
		}

		List<File> oldData = mData;
		mData = data;
		
		if (isStarted())
			super.deliverResult(data);

		if (oldData != null && oldData != data)
			onReleaseResources(oldData);
	}

	@Override
	protected void onStartLoading() {
		if (mData != null)
			deliverResult(mData);

		if (mFileObserver == null) {
			mFileObserver = new FileObserver(mPath, FILE_OBSERVER_MASK) {
				@Override
				public void onEvent(int event, String path) {
					onContentChanged();	
				}
			};
		}
		mFileObserver.startWatching();
		
		if (takeContentChanged() || mData == null)
			forceLoad();
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		onStopLoading();

		if (mData != null) {
			onReleaseResources(mData);
			mData = null;
		}
	}

	@Override
	public void onCanceled(List<File> data) {
		super.onCanceled(data);

		onReleaseResources(data);
	}

	protected void onReleaseResources(List<File> data) {
		
		if (mFileObserver != null) {
			mFileObserver.stopWatching();
			mFileObserver = null;
		}
	}
}