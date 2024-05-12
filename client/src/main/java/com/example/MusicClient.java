package com.example;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

import MusicServer.MusicManagerPrx;
import MusicServer.MusicNotFoundError;


// import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
// import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
// import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
// import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class MusicClient {

    private Scanner scanner;
    private MusicManagerPrx musicManager;
    // private AudioPlayerComponent audioPlayer;

    public MusicClient(Communicator communicator) {
        // audioPlayer = new AudioPlayerComponent();
        musicManager = MusicManagerPrx.checkedCast(communicator.stringToProxy("MusicManager:default -h 192.168.0.19 -p 10000"));
        if (musicManager == null) {
            throw new RuntimeException("Invalid proxy");
        }

        scanner = new Scanner(System.in);
    }

    public static void main(String[] args)
    {
        String[] customArgs = new String[]{"--Ice.MessageSizeMax=0"};
        try (Communicator communicator = Util.initialize(customArgs)) {
            MusicClient client = new MusicClient(communicator);
            client.start();
        }
    }

    public void start() {
        System.out.println("Bienvenue sur le serveur de musique !");
        String command;
        while (true) {
            command = scanner.nextLine();
            processCommand(command);
        }
    }

    public void processCommand(String command) {
        String[] commandParts = command.split(" ");
    
        switch (commandParts[0]) {
            case "help":
                System.out.println("Liste des commandes disponibles :");
                System.out.println("list - Affiche la liste de toutes les musiques disponibles");
                System.out.println("list [titre] - Affiche la liste des musiques correspondant au titre donné");
                System.out.println("listTitle [titre] - Affiche la liste des musiques correspondant au titre donné");
                System.out.println("listArtist [artiste] - Affiche la liste des musiques correspondant à l'artiste donné");
                System.out.println("add [fichier] [titre] [artiste] - Ajoute une nouvelle musique");
                System.out.println("delete [titre] [artiste] - Supprime une musique existante");
                System.out.println("update [titre] [artiste] [nouveau titre] [nouvel artiste] - Met à jour une musique existante");
                System.out.println("play [titre] [artiste] - Joue une musique");
                System.out.println("pause - Met en pause la musique en cours de lecture");
                System.out.println("stop - Arrête la musique en cours de lecture");
                System.out.println();
                break;
            case "list":
                if (commandParts.length == 1) {
                    listAllMusic();
                } else if (commandParts.length == 2) {
                    listMusicByTitle(commandParts[1]);
                } else {
                    System.out.println("Nombre incorrect d'arguments pour la commande 'list'. Veuillez saisir 'list' ou 'list [titre]'.");
                }
                System.out.println();
                break;
            case "listTitle":
                if (commandParts.length == 2) {
                    listMusicByTitle(commandParts[1]);
                } else {
                    System.out.println("Nombre incorrect d'arguments pour la commande 'listTitle'. Veuillez saisir 'listTitle [titre]'.");
                }
                System.out.println();
                break;
            case "listArtist":
                if (commandParts.length == 2) {
                    listMusicByArtist(commandParts[1]);
                } else {
                    System.out.println("Nombre incorrect d'arguments pour la commande 'listArtist'. Veuillez saisir 'listArtist [artiste]'.");
                }
                System.out.println();
                break;
            case "add":
                if (commandParts.length == 4) {
                    addMusic(commandParts[1], commandParts[2], commandParts[3]);
                } else {
                    System.out.println("Nombre incorrect d'arguments pour la commande 'add'. Veuillez saisir 'add [fichier] [titre] [artiste]'.");
                }
                System.out.println();
                break;
            case "delete":
                if (commandParts.length == 3) {
                    deleteMusic(commandParts[1], commandParts[2]);
                } else {
                    System.out.println("Nombre incorrect d'arguments pour la commande 'delete'. Veuillez saisir 'delete [titre] [artiste]'.");
                }
                System.out.println();
                break;
            case "update":
                if (commandParts.length == 5) {
                    updateMusic(commandParts[1], commandParts[2], commandParts[3], commandParts[4]);
                } else {
                    System.out.println("Nombre incorrect d'arguments pour la commande 'update'. Veuillez saisir 'update [titre] [artiste] [nouveau titre] [nouvel artiste]'.");
                }
                System.out.println();
                break;
            case "play":
                if (commandParts.length == 3) {
                    playMusic(commandParts[1], commandParts[2]);
                } else {
                    System.out.println("Nombre incorrect d'arguments pour la commande 'play'. Veuillez saisir 'play [titre] [artiste]'.");
                }
                System.out.println();
                break;
            case "pause":
                pauseMusic();
                System.out.println();
                break;
            case "stop":
                stopMusic();
                System.out.println();
                break;
            default:
                System.out.println("Commande inconnue. Veuillez saisir une commande valide.");
                System.out.println();
        }
    }



    public void addMusic(String filename, String title, String artist) {
        try {
            byte[] file = Files.readAllBytes(Paths.get(filename));
            musicManager.addMusic(file, title, artist);
            System.out.println("Musique ajoutée avec succès : '" + title + "' par " + artist);
        } catch (IOException e) {
            System.out.println("Erreur dans l'ajout du fichier: " + e.getMessage());
        }
    }
    
    public void listAllMusic() {
        MusicServer.MusicInfo[] musicInfos = musicManager.listAllMusic();
        if (musicInfos.length == 0) {
            System.out.println("Aucun titre disponible");
        } else {
            System.out.println("Liste des titres disponibles: ");
            for (MusicServer.MusicInfo musicInfo : musicInfos) {
                System.out.println("- " + musicInfo.title + " par " + musicInfo.artist);
            }
        }
    }
    
    public void listMusicByTitle(String title) {
        MusicServer.MusicInfo[] musicInfos = musicManager.listMusicByTitle(title);
        if (musicInfos.length == 0) {
            System.out.println("Aucun titre pour " + title);
        } else {
            System.out.println("Liste des titres disponible pour " + title + ": ");
            for (MusicServer.MusicInfo musicInfo : musicInfos) {
                System.out.println("- " + musicInfo.title + " par " + musicInfo.artist);
            }
        }
    }
    
    public void listMusicByArtist(String artist) {
        MusicServer.MusicInfo[] musicInfos = musicManager.listMusicByArtist(artist);
        if (musicInfos.length == 0) {
            System.out.println("Aucun titre de " + artist);
        } else {
            System.out.println("Liste des titres disponible de " + artist + ": ");
            for (MusicServer.MusicInfo musicInfo : musicInfos) {
                System.out.println("- " + musicInfo.title + " par " + musicInfo.artist);
            }
        }
    }
    
    public void deleteMusic(String title, String artist) {

        try { 
            musicManager.deleteMusic(title, artist);
            System.out.println("Musique supprimée avec succès : '" + title + "' par " + artist);
        } catch (MusicServer.MusicNotFoundError e) {
            System.out.println("La musique n'a pas été trouvée.");
        }
    }
    
    public void updateMusic(String title, String artist, String newTitle, String newArtist) {
        musicManager.updateMusic(title, artist, newTitle, newArtist);
        System.out.println("Musique mise à jour avec succès : de '" + title + "' par " + artist + " à '" + newTitle + "' par " + newArtist);
    }
    
    public void playMusic(String title, String artist) {
        try {
            byte[] data = musicManager.playMusic(title, artist);
        //     File tempFile = File.createTempFile("music-", ".mp3");
        //     tempFile.deleteOnExit();
        //     FileOutputStream outputStream = new FileOutputStream(tempFile);
        //     outputStream.write(data);

            
        //     audioPlayer.mediaPlayer().media().play(tempFile.getAbsolutePath());
            System.out.println("En train de jouer '" + title + "' par " + artist);
        } catch (MusicServer.MusicNotFoundError e) {
            System.out.println("La musique n'a pas été trouvée.");
        // } catch (IOException e) {
        //     System.out.println("Erreur : " + e.getMessage());
        }
    }

    private boolean isPaused = false;
    
    public void pauseMusic() {
        musicManager.pauseMusic();
        // audioPlayer.mediaPlayer().controls().pause();
        // if (!isPaused) {
        //     isPaused = true;
        //     System.out.println("Musique en pause");
        // } else {
        //     isPaused = false;
        //     System.out.println("Musique reprise");
        // }
    }
    
    public void stopMusic() {
        // audioPlayer.mediaPlayer().controls().stop();
        // System.out.println("Musique arrêtée");
    }

}