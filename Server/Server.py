import Ice, os, uuid, MusicServer, sqlite3, vlc, time
from MusicServer import MusicNotFoundError
 
class MusicManagerI(MusicServer.MusicManager):
    def __init__(self):
        self.instance = vlc.Instance(['--rtsp-host=192.168.0.19'])
        self.player = self.instance.media_player_new()
        self.music_dir = "music/"
        self.db_dir = "music.db"

    def listAllMusic(self, current=None):
        conn = sqlite3.connect(self.db_dir)
        cursor = conn.cursor()
        cursor.execute("SELECT title, artist FROM music")
        result = [MusicServer.MusicInfo(title=row[0], artist=row[1]) for row in cursor.fetchall() if row[0] and row[1]]
        conn.close()
        return result

    def listMusicByTitle(self, title, current=None):
        conn = sqlite3.connect(self.db_dir)
        cursor = conn.cursor()
        cursor.execute("SELECT title, artist FROM music WHERE title LIKE ?", ('%' + title + '%',))
        result = [MusicServer.MusicInfo(title=row[0], artist=row[1]) for row in cursor.fetchall()]
        conn.close()
        return result
    
    def listMusicByArtist(self, artist, current=None):
        conn = sqlite3.connect(self.db_dir)
        cursor = conn.cursor()
        cursor.execute("SELECT title, artist FROM music WHERE artist LIKE ?", ('%' + artist + '%',))
        result = [MusicServer.MusicInfo(title=row[0], artist=row[1]) for row in cursor.fetchall()]
        conn.close()
        return result

    def addMusic(self, file, title, artist, current=None):
        try:
            file_name = str(uuid.uuid4()) + ".mp3"
            with open(self.music_dir + file_name, "wb") as f:
                f.write(file)

            conn = sqlite3.connect(self.db_dir)
            cursor = conn.cursor()
            cursor.execute("""
                INSERT INTO music (title, artist, file_name)
                VALUES (?, ?, ?)
            """, (title, artist, file_name))
            conn.commit()
            conn.close()
        except Exception as e:
            print(e)

    def deleteMusic(self, title, artist, current=None):
        conn = sqlite3.connect(self.db_dir)
        cursor = conn.cursor()
        cursor.execute("SELECT file_name FROM music WHERE title = ? AND artist = ?", (title, artist))
        file_name = cursor.fetchone()
        if file_name is None:
            raise MusicNotFoundError("La musique n'a pas été trouvée.")
        else:
            file_name = cursor.fetchone()
        cursor.execute("DELETE FROM music WHERE title = ? AND artist = ?", (title, artist))
        conn.commit()
        os.remove(self.music_dir + file_name)
        conn.close()

    def updateMusic(self, title, artist, newTitle, newArtist, current=None):
        conn = sqlite3.connect(self.db_dir)
        cursor = conn.cursor()
        cursor.execute("UPDATE music SET title = ?, artist = ? WHERE title = ? AND artist = ?", (newTitle, newArtist, title, artist))
        conn.commit()
        conn.close()

    def playMusic(self, title, artist, current=None):
        conn = sqlite3.connect(self.db_dir)
        cursor = conn.cursor()
        cursor.execute("SELECT file_name FROM music WHERE title = ? AND artist = ?", (title, artist))
        result = cursor.fetchone()
        if result is None:
            raise MusicNotFoundError("La musique n'a pas été trouvée.")
        file_name = result[0]
        conn.close()

        # Créez un objet MediaPlayer pour lire le fichier audio
        self.player.stop()

        # Create a new media and set it to the player
        media = self.instance.media_new_path("music/" + file_name)
        url = f"rtsp://:554/stream"
        options = f":sout=#transcode{{acodec=mpga,ab=128,channels=2,samplerate=44100}}:rtp{{mux=ts,sdp={url}}}"
        media.add_option(options)
        self.player.set_media(media)

        # Play the new media
        self.player.play()
        print("aaa")

    def pauseMusic(self, current=None):
        self.player.pause()

props = Ice.createProperties()
props.setProperty("Ice.MessageSizeMax", "0")
initData = Ice.InitializationData()
initData.properties = props
with Ice.initialize(initData) as communicator:
    conn = sqlite3.connect("music.db")
    cursor = conn.cursor()
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS music (
            id INTEGER PRIMARY KEY,
            title TEXT NOT NULL,
            artist TEXT NOT NULL,
            file_name TEXT NOT NULL
        )
    """)
    conn.commit()
    conn.close()

    adapter = communicator.createObjectAdapterWithEndpoints("MusicAdapter", "default -h 192.168.0.19 -p 10000")
    object = MusicManagerI()
    adapter.add(object, communicator.stringToIdentity("MusicManager"))
    adapter.activate()
    communicator.waitForShutdown()