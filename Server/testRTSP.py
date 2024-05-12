import asyncio
from aiortc import RTCPeerConnection, RTCSessionDescription
from aiortc.contrib.media import MediaBlackhole, MediaPlayer, MediaRecorder


async def offer_answer(pc, offer):
    await pc.setRemoteDescription(offer)
    if offer.type == "offer":
        answer = await pc.createAnswer()
        await pc.setLocalDescription(answer)
        return pc.localDescription


async def run(pc, player):
    # create a MediaStreamTrack with audio from the microphone
    if player:
        audio = player.audio
    else:
        # blackhole, no audio
        audio = MediaBlackhole()

    await pc.setRemoteDescription(await pc.createOffer())
    await pc.setLocalDescription(await pc.createAnswer())

    @pc.on("track")
    def on_track(track):
        print("Track %s received" % track.kind)
        if track.kind == "audio":
            audio.addTrack(track)

    await asyncio.sleep(300)


async def main():
    pc = RTCPeerConnection()

    # Create a media player for audio file
    player = MediaPlayer("music/4143a962-1103-4368-84dd-741eb2e6a8a2.mp3")

    # Add the audio track to the peer connection
    pc.addTrack(player.audio)

    # Generate an initial offer
    offer = await pc.createOffer()
    await pc.setLocalDescription(offer)

    # Pass the initial offer to offer_answer
    answer = await offer_answer(pc, offer)

    # run
    await asyncio.gather(pc.setRemoteDescription(answer))

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        pass