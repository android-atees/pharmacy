package in.ateesinfomedia.relief.recorder;

public interface OnAudioRecordListener {

    void onRecordFinished(RecordingItem recordingItem);

    void onError(int errorCode);

    void onRecordingStarted();

}
