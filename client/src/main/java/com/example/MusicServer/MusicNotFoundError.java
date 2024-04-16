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

public class MusicNotFoundError extends com.zeroc.Ice.UserException
{
    public MusicNotFoundError()
    {
        this.message = "";
    }

    public MusicNotFoundError(Throwable cause)
    {
        super(cause);
        this.message = "";
    }

    public MusicNotFoundError(String message)
    {
        this.message = message;
    }

    public MusicNotFoundError(String message, Throwable cause)
    {
        super(cause);
        this.message = message;
    }

    public String ice_id()
    {
        return "::MusicServer::MusicNotFoundError";
    }

    public String message;

    /** @hidden */
    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::MusicServer::MusicNotFoundError", -1, true);
        ostr_.writeString(message);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _readImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        message = istr_.readString();
        istr_.endSlice();
    }

    /** @hidden */
    public static final long serialVersionUID = 7744299730206333529L;
}