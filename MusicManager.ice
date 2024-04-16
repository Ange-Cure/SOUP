module MusicServer {
    exception MusicNotFoundError {
        string message;
    }
    sequence<byte> Bytes;
    struct MusicInfo {
        string title;
        string artist;
    }
    sequence<MusicInfo> seqMusicInfo;
    interface MusicManager {
        seqMusicInfo listAllMusic();
        seqMusicInfo listMusicByTitle(string title);
        seqMusicInfo listMusicByArtist(string artist);
        void addMusic(Bytes file, string title, string artist);
        void deleteMusic(string title, string artist) throws MusicNotFoundError;
        void updateMusic(string title, string artist, string newTitle, string newArtist);
        Bytes playMusic(string title, string artist) throws MusicNotFoundError;
    }
}
