package kir.util;

import kir.util.media.MediaStreamer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static final Scanner scanner = new Scanner(System.in);
    private static Path mediaSourcePath;

    public static void main(String[] args) throws IOException, InterruptedException {
        Printer.print("Stream address: ");
        var address = scanner.nextLine().trim();

        Printer.print("Stream port: ");
        var port = scanner.nextInt();


        var chooser = new JFileChooser();
        chooser.setDialogTitle("Choose media source directory");
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showDialog(null, "Add");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            mediaSourcePath = chooser.getSelectedFile().toPath();
        }

        var streamer = new MediaStreamer(address, port);
        streamer.addVideoDirectory(mediaSourcePath.toAbsolutePath().toString());
        streamer.setVideoCodecName(args.length == 1 ? args[0] : "qsv");

        Printer.success("Directory scan completed. Found " + streamer.getQueue().size() + " streamable files.\n");

        Printer.println("\nSTREAMING\n");
        while (streamer.stream()) {
            Printer.info("\t - " + streamer.getCurrentFileName() + " DONE.");
        }

        Printer.success("DONE STREAMING");
    }
}