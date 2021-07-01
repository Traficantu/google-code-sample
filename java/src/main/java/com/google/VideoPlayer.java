package com.google;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  protected ArrayList<VideoPlaylist> videoPlaylists = new ArrayList<>();
  protected String currentlyplaying = "";
  protected Random ran = new Random();
  protected Boolean isPaused = false;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public List<Video> sortLex(List<Video> list)
  {
    for (int i = 0; i < list.size(); i++) {
      for (int j = 0; j < list.size(); j++) {
        if (list.get(i).getTitle().compareToIgnoreCase(list.get(j).getTitle()) < 0) {
          Video temp = list.get(i);
          list.set(i, list.get(j));
          list.set(j, temp);
        }
      }
    }
      return list;
  }

  public ArrayList<VideoPlaylist> sortLex(ArrayList<VideoPlaylist> list)
  {
    for (int i = 0; i < list.size(); i++) {
      for (int j = 0; j < list.size(); j++) {
        if (list.get(i).name.compareToIgnoreCase(list.get(j).name) < 0) {
          VideoPlaylist temp = list.get(i);
          list.set(i, list.get(j));
          list.set(j, temp);
        }
      }
    }
      return list;
  }
  public void showAllVideos() {
    List<Video> videoList = videoLibrary.getVideos();

    System.out.println("Here's a list of all available videos:");

    for (int i = 0; i < videoList.size(); i++) {
      for (int j = 0; j < videoList.size(); j++) {
        if (videoList.get(i).getTitle().compareToIgnoreCase(videoList.get(j).getTitle()) < 0) {
          Video temp = videoList.get(i);
          videoList.set(i, videoList.get(j));
          videoList.set(j, temp);
        }
      }
    }

    for (Video video : videoList) {
      String videoDetails = "" + video.getTitle() + " (" + video.getVideoId() + ") " + video.getTags();
      videoDetails = formatVideoDetails(videoDetails);
      System.out.println(videoDetails);
    }
  }

  public Boolean isCurrentlyPlaying() {
    return currentlyplaying.isEmpty();
  }

  public String formatVideoDetails(String details) {
    return details.replace(",", "");
  }

  public void playVideo(String videoId) {
    List<Video> videoList = videoLibrary.getVideos();
    int videoFound = 0;

    for (Video video : videoList) {
      if (video.getVideoId().equals(videoId) && Boolean.TRUE.equals(isCurrentlyPlaying())) {
        System.out.println("Playing video: " + video.getTitle());
        currentlyplaying = video.getTitle();
        videoFound = 1;
      } else if (video.getVideoId().equals(videoId) && Boolean.FALSE.equals(isCurrentlyPlaying())) {
        System.out.println(" Stopping video: " + currentlyplaying + "\n Playing video: " + video.getTitle());
        currentlyplaying = video.getTitle();
        videoFound = 1;
        isPaused = false;
      }
    }
    if (videoFound == 0) {
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if (Boolean.FALSE.equals(isCurrentlyPlaying())) {
      System.out.println("Stopping video: " + currentlyplaying);
      currentlyplaying = "";
    } else
      System.out.println("Cannot stop video: No video is currently playing");
  }

  public void playRandomVideo() {
    List<Video> videoList = videoLibrary.getVideos();
    Video video = videoList.get((ran.nextInt(videoList.size())));
    if (Boolean.FALSE.equals(isCurrentlyPlaying()))
      stopVideo();
    playVideo(video.getVideoId());
  }

  public void pauseVideo() {

    if ((Boolean.FALSE.equals(isCurrentlyPlaying()))) {
      if ((Boolean.TRUE.equals(isPaused)))
        System.out.println("Video already paused: " + currentlyplaying);
      else {
        System.out.println("Pausing video: " + currentlyplaying);
        isPaused = true;
      }
    } else {
      System.out.println("Cannot pause video: No video is currently playing");
    }

  }

  public void continueVideo() {
    if (Boolean.FALSE.equals(isCurrentlyPlaying())) {
      if (Boolean.FALSE.equals(isPaused)) {
        System.out.println("Cannot continue video: Video is not paused");
      } else {
        System.out.println("Continuing video: " + currentlyplaying);
        isPaused = false;
      }
    } else {
      System.out.println("Cannot continue video: No video is currently playing");
    }

  }

  public void showPlaying() {
    List<Video> videoList = videoLibrary.getVideos();
    if (Boolean.TRUE.equals(isCurrentlyPlaying()))
      System.out.println("No video is currently playing");
    else
      for (Video video : videoList) {
        if (video.getTitle().contentEquals(currentlyplaying)) {
          String videoDetails = "" + video.getTitle() + " (" + video.getVideoId() + ") " + video.getTags();
          videoDetails = formatVideoDetails(videoDetails);
          String paused = " - PAUSED";
          if (Boolean.TRUE.equals(isPaused)) {
            System.out.println("Currently playing: " + videoDetails + paused);
          } else
            System.out.println("Currently playing: " + videoDetails);
        }
      }
  }

  public void createPlaylist(String playlistName) {
    int foundPlaylist = 0;
    for (VideoPlaylist videoPlaylist : videoPlaylists) {
      if (playlistName.equalsIgnoreCase(videoPlaylist.name)) {
        System.out.println("Cannot create playlist: A playlist with the same name already exists");
        foundPlaylist = 1;
      }
    }

    if (foundPlaylist != 1) {
      VideoPlaylist newPlaylist = new VideoPlaylist(playlistName);
      videoPlaylists.add(newPlaylist);
      System.out.println("Successfully created new playlist: " + newPlaylist.name);
    }
  }

  public Boolean isPlaylist(String playlistName) {
    for (VideoPlaylist videoPlaylist : videoPlaylists) {
      if (playlistName.equalsIgnoreCase(videoPlaylist.name))
        return true;
    }
    return false;
  }

  public VideoPlaylist getPlaylist(String playlistName) {
    for (VideoPlaylist videoPlaylist : videoPlaylists) {
      if (videoPlaylist.name.equalsIgnoreCase(playlistName))
        return videoPlaylist;
    }

    return null;
  }

  public int videoInPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = getPlaylist(playlistName);
    Video video;
    for (int i = 0; i < playlist.videos.size(); i++) {
      video = playlist.videos.get(i);
      if (video.getVideoId().contentEquals(videoId))
        return i;
    }

    return -1;
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    Boolean noPlaylist = Boolean.FALSE.equals(isPlaylist(playlistName));
    Boolean noVideo = (videoLibrary.getVideo(videoId) == null);
    String errorMSG = "Cannot add video to ";
    VideoPlaylist playlist = getPlaylist(playlistName);

    if (noPlaylist || noVideo) {
      if (noPlaylist && noVideo || noPlaylist) {
        System.out.println(errorMSG + playlistName + ": Playlist does not exist");
      } else if (noVideo) {
        System.out.println(errorMSG + playlistName + ": Video does not exist");
      }
    } else {
      if (videoInPlaylist(playlistName, videoId) != -1)
        System.out.println(errorMSG + playlistName + ": Video already added");
      else {
        playlist.videos.add(videoLibrary.getVideo(videoId));
        System.out.println("Added video to " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
      }

    }
  }

  public void showAllPlaylists() {
    if (!(videoPlaylists.isEmpty())) {
      System.out.println("Showing all playlists:");

      videoPlaylists = sortLex(videoPlaylists);

      for (VideoPlaylist videoPlaylist : videoPlaylists) {
        System.out.println("  " + videoPlaylist.name);
      }
    } else {
      System.out.println("No playlists exist yet");
    }
  }

  public void showPlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylist(playlistName);

    if (playlist != null) {
      System.out.println("Showing playlist: " + playlistName);
      if (playlist.videos.isEmpty()) {
        System.out.println("  No videos here yet");
      } else {
        for (Video video : playlist.videos) {
          String videoDetails = "  " + video.getTitle() + " (" + video.getVideoId() + ") " + video.getTags();
          videoDetails = formatVideoDetails(videoDetails);
          System.out.println(videoDetails);
        }
      }
    } else {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }

  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = getPlaylist(playlistName);
    String errorMSG = "Cannot remove video from ";
    if (playlist == null) {
      System.out.println(errorMSG + playlistName + ": Playlist does not exist");

    } else {
      if (videoLibrary.getVideo(videoId) == null) {
        System.out.println(errorMSG + playlistName + ": " + "Video does not exist");
      } else {
        int videoIndex = videoInPlaylist(playlistName, videoId);
        if (videoIndex >= 0) {
          System.out.println("Removed video from " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
          playlist.videos.remove(videoIndex);
        } else {
          System.out.println(errorMSG + playlistName + ": " + "Video is not in playlist");
        }
      }
    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylist(playlistName);
    if (playlist == null) {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist ");
    } else {
      for (int i = playlist.videos.size() - 1; i >= 0; i--) {
        playlist.videos.remove(i);
      }
      System.out.println("Successfully removed all videos from " + playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist playlist = getPlaylist(playlistName);
    if (playlist == null) {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist ");
    } else {
      System.out.println("Deleted playlist: " + playlistName);
      for (int i = 0; i < videoPlaylists.size(); i++) {
        if (videoPlaylists.get(i).name.equalsIgnoreCase(playlistName)) {
          videoPlaylists.remove(i);
        }

      }
    }

  }

  public void searchVideos(String searchTerm) {
    List<Video> videos = videoLibrary.getVideos();
    List<Video> videosFound = new ArrayList<>();
    var scanner = new Scanner(System.in);
    int counter = 0;
    String regex = "\\d+";

    for (Video video : videos) {
      String title = video.getTitle();
      String term = searchTerm.toLowerCase();
      title = title.toLowerCase();

      if (title.contains(term))
        videosFound.add(video);
    }

    if (!(videosFound.isEmpty())) {
      System.out.println("Here are the results for cat:");
      videosFound = sortLex(videosFound);
      for (int i = 0; i < videosFound.size(); i++) {
        Video video = videosFound.get(i);
        String tags = "" + video.getTags();
        System.out.println(
            "" + (i + 1) + ") " + video.getTitle() + " (" + video.getVideoId() + ") " + formatVideoDetails(tags));
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      while (true) {
        counter ++;
        String input = scanner.nextLine();
        if (!(input.isEmpty())) {
          if (!(input.matches(regex))) {
            return;
          } else {
            int videoIndex = Integer.parseInt(input) - 1;
            if(videoIndex >= videosFound.size())
              return;
            Video videoToPlay = videosFound.get(videoIndex);
            playVideo(videoToPlay.getVideoId());
            return;
          }
        }
        if(counter > 0)
          return;
      }
    } else {
      System.out.println("No search results for " + searchTerm);
    }

  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}