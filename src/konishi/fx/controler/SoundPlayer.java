package konishi.fx.controler;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class SoundPlayer{
    private AudioFormat format = null;
    private DataLine.Info info = null;
    private Clip clip = null;
    
    private Thread thread;

    public SoundPlayer(String pathname){
        File file = new File(pathname);

        try{
            format = AudioSystem.getAudioFileFormat(file).getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(AudioSystem.getAudioInputStream(file));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void play(){
        thread = new Thread(){
            public void run(){
                int time = clip.getFrameLength();
                clip.start();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        thread.start();
    }
    
    public void stopThread() {
    	clip.close();
    }
}