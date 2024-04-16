//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.9
//
// <auto-generated>
//
// Generated from file `MusicManager.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package MusicServer;

/**
 * Helper class for marshaling/unmarshaling seqMusicInfo.
 **/
public final class seqMusicInfoHelper
{
    public static void write(com.zeroc.Ice.OutputStream ostr, MusicInfo[] v)
    {
        if(v == null)
        {
            ostr.writeSize(0);
        }
        else
        {
            ostr.writeSize(v.length);
            for(int i0 = 0; i0 < v.length; i0++)
            {
                MusicInfo.ice_write(ostr, v[i0]);
            }
        }
    }

    public static MusicInfo[] read(com.zeroc.Ice.InputStream istr)
    {
        final MusicInfo[] v;
        final int len0 = istr.readAndCheckSeqSize(2);
        v = new MusicInfo[len0];
        for(int i0 = 0; i0 < len0; i0++)
        {
            v[i0] = MusicInfo.ice_read(istr);
        }
        return v;
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<MusicInfo[]> v)
    {
        if(v != null && v.isPresent())
        {
            write(ostr, tag, v.get());
        }
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, MusicInfo[] v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            seqMusicInfoHelper.write(ostr, v);
            ostr.endSize(pos);
        }
    }

    public static java.util.Optional<MusicInfo[]> read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            MusicInfo[] v;
            v = seqMusicInfoHelper.read(istr);
            return java.util.Optional.of(v);
        }
        else
        {
            return java.util.Optional.empty();
        }
    }
}