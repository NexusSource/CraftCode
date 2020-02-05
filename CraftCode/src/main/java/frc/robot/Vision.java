package frc.robot;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Vision {
    { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    CvSink sink;
    CvSource output;

    CvSource rgbOutput;
    Mat rgbRaw = new Mat();
    Mat rgbMod = new Mat();
    Mat otsuRaw = new Mat();
    Mat otsuMod = new Mat();

    VisionThread thread;
    CameraServer server = CameraServer.getInstance();

    public void SetupVision() {
        sink = server.getVideo();
        output = server.putVideo("Processed: OTSU", 640, 480);
        rgbOutput = server.putVideo("Processed: RGB", 640, 480);
    }

    public void UpdateVision() {
        Otsu();
        RGB();
    }

    private void Otsu() {
        sink.grabFrame(otsuRaw);
        Imgproc.cvtColor(otsuRaw, otsuMod, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(otsuMod, otsuMod, 0, 255, Imgproc.THRESH_OTSU);
        output.putFrame(otsuMod);
    }

    private void RGB() {
        sink.grabFrame(rgbRaw);
        Imgproc.cvtColor(rgbRaw, rgbMod, Imgproc.COLOR_BGR2RGB);
        Core.inRange(rgbMod, new Scalar(140, 140, 0), new Scalar(255, 255, 110), rgbMod);
        rgbOutput.putFrame(rgbMod);
    }

    public void MoveRobotByColor(DifferentialDrive d) {
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(rgbMod, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        for (int i = 0; i < contours.size(); i++) {
            double contourArea = Imgproc.contourArea(contours.get(i));
            if (contourArea > 200) {
                Rect boundRect = Imgproc.boundingRect(contours.get(i));
                double centerX = boundRect.x + (boundRect.width / 2);
                double centerY = boundRect.y + (boundRect.height / 2);
                System.out.println("CENTER X: " + centerX);
                if (centerX < 55) {
                    d.arcadeDrive(0f, -.77f);
                    return;
                }
                else if (centerX > 105){
                    d.arcadeDrive(0f, .77f);

                    return;
                }
                    
                else {
                    d.arcadeDrive(-.68f, 0f);
                    return;
                } 
            }

            
        }

        /*for (int i = 0; i < contours.size(); i++) {
            double contourArea = Imgproc.contourArea(contours.get(i));
            System.out.println(contourArea);
            if (contourArea < 200)
                return;
            Rect boundRect = Imgproc.boundingRect(contours.get(i));
            double centerX = boundRect.x + (boundRect.width / 2);
            double centerY = boundRect.y + (boundRect.height / 2);
            System.out.println("CENTER Y: " + centerY);

        }*/
    }
}