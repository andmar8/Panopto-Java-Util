    /*
     * This file is part of Panopto-Java-Util.
     *
     * Panopto-Java-Util is free software: you can redistribute it and/or modify
     * it under the terms of the GNU General Public License as published by
     * the Free Software Foundation, either version 3 of the License, or
     * (at your option) any later version.
     *
     * Panopto-Java-Util is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     * GNU General Public License for more details.
     *
     * You should have received a copy of the GNU General Public License
     * along with Panopto-Java-Util.  If not, see <http://www.gnu.org/licenses/>.
     *
     * Copyright: Andrew Martin, Newcastle University
     *
     */
package uk.ac.ncl.panopto;

import com.panopto.remoterecorder.RemoteRecorderManagementStub;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.ArrayOfRecorderSettings;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.AuthenticationInfo;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.GetRemoteRecordersByExternalId;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.Guid;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.RecorderSettings;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.RemoteRecorder;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.ScheduleRecording;
import com.panopto.session.SessionManagementStub;
import com.panopto.session.SessionManagementStub.ArrayOfFolder;
import com.panopto.session.SessionManagementStub.ArrayOfstring;
import com.panopto.session.SessionManagementStub.Folder;
import com.panopto.session.SessionManagementStub.GetFoldersByExternalId;
import com.panopto.session.SessionManagementStub.GetFoldersByExternalIdResponse;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class PanoptoSessionStringListParser
{
    private static void csvStyleOutput(String output)
    {
        System.out.print(output+",");
    }

    static private GregorianCalendar getCalendarFromString(String dateTime) throws Exception
    {
        //The times being set should return in UTC for passing to panopto
        GregorianCalendar gc = new GregorianCalendar();
        TimeZone tz = gc.getTimeZone();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/mm/dd");
        Date convertedDate = dateFormat.parse(dateTime);
        tz.inDaylightTime(convertedDate);

        if(tz.inDaylightTime(convertedDate))
        {
            throw new Exception("Date is in DST");
        }
        return gc;
    }

    static private Guid getFolderIdFromFolderExternalId(SessionManagementStub stub, GetFoldersByExternalId gfbei) throws Exception
    {
        Guid guid = new Guid();
        List<Folder> fl = null;
        try
        {
            fl = Arrays.asList(stub.getFoldersByExternalId(gfbei).getGetFoldersByExternalIdResult().getFolder());
        }
        catch(NullPointerException npe)
        {
            npe.printStackTrace();
            throw new Exception("Error: Folder not found!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Iterator<Folder> fi = fl.iterator();
        while(fi.hasNext())
        {
            guid.setGuid(fi.next().getId().getGuid());
            break;
        }
        return guid;
    }

    static private ArrayOfRecorderSettings getRemoteRecorderSettingsForLocation(RemoteRecorderManagementStub stub,
            GetRemoteRecordersByExternalId grrbei) throws Exception
    {
        RecorderSettings rs = null;
        ArrayOfRecorderSettings aors = new ArrayOfRecorderSettings();
        List<RecorderSettings> rsl = new ArrayList<RecorderSettings>();
        RemoteRecorder rr = null;
        List<RemoteRecorder> rrl = Arrays.asList(stub.getRemoteRecordersByExternalId(grrbei).getGetRemoteRecordersByExternalIdResult().getRemoteRecorder());
        Iterator<RemoteRecorder> rri = rrl.iterator();
        while(rri.hasNext())
        {
            rr = rri.next();
            rs = new RecorderSettings();
            rs.setRecorderId(rr.getId());
            rs.setSuppressPrimary(rr.getName().endsWith("-S")?true:false);
            rs.setSuppressSecondary(false);
            rsl.add(rs);
        }
        aors.setRecorderSettings(rsl.toArray(new RecorderSettings[rsl.size()]));
        return aors;
    }

    static public Map<String,List<ScheduleRecording>> parse(RemoteRecorderManagementStub rrmStub, RemoteRecorderManagementStub.AuthenticationInfo rrmAuth, SessionManagementStub smStub, SessionManagementStub.AuthenticationInfo smAuth, List<List<String>> parsedSchedules) throws NumberFormatException, RemoteException
    {
        //create sessions**********************************************************************
        System.out.println("Attempting to create sessions from parsed csv...");

        Map<String,List<ScheduleRecording>> panoptoScheduleRecordings = new HashMap<String,List<ScheduleRecording>>();
        Iterator<List<String>> listItrtr = parsedSchedules.iterator();
        Iterator<String> stringItrtr = null;
        ScheduleRecording scheduleRecording = null;

        /*
         * Recorder settings here...
         */

        int columnNumber = 0;
        String current = "";
        GregorianCalendar gc;
        SimpleDateFormat df;
        RemoteRecorderManagementStub.ArrayOfstring rrmAos;
        SessionManagementStub.ArrayOfstring smAos;
        System.out.println("name,folderExternalId,start,end,location");
        String externalId = "";
        while(listItrtr.hasNext())
        {
            try
            {
                scheduleRecording = new ScheduleRecording();
                scheduleRecording.setAuth(rrmAuth);
                //Default settings for all sessions...
                scheduleRecording.setIsBroadcast(false);

                columnNumber = 0;
                stringItrtr = listItrtr.next().iterator();
                //Custom settings for each booking...
                while(stringItrtr.hasNext())
                {
                    current = stringItrtr.next();
                    switch(columnNumber)
                    {
                        //drop venues that aren't valid
                        //drop schedules that are in the past

                        //name
                        case 0: csvStyleOutput(current);
                                scheduleRecording.setName(current);
                                break;
                        //folderExternalId
                        case 1: csvStyleOutput(current);

                                GetFoldersByExternalId gfbei = new GetFoldersByExternalId();
                                gfbei.setAuth(smAuth);
                                smAos = new SessionManagementStub.ArrayOfstring();
                                smAos.addString(current);
                                gfbei.setFolderExternalIds(smAos);
                                scheduleRecording.setFolderId(getFolderIdFromFolderExternalId(smStub, gfbei)); //####
                                break;
                        //startDateTime
                        case 2: df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date startDate = df.parse(current);
                                gc = new GregorianCalendar();
                                gc.setTime(startDate);
                                gc.add(Calendar.MINUTE, 5);   // double check this with andrew and mark
                                csvStyleOutput(startDate.toString());
                                scheduleRecording.setStart(gc);
                                break;
                        //endDateTime
                        case 3: df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date endDate = df.parse(current);
                                gc = new GregorianCalendar(); //getCalendarFromString(current);
                                gc.setTime(endDate);
                                csvStyleOutput(endDate.toString());
                                scheduleRecording.setEnd(gc);
                                break;
                        //remote recorders
                        case 4: csvStyleOutput(current);
                                GetRemoteRecordersByExternalId grrbei = new GetRemoteRecordersByExternalId();
                                grrbei.setAuth(rrmAuth);
                                rrmAos = new RemoteRecorderManagementStub.ArrayOfstring();
                                rrmAos.addString(current.replaceAll("[ ,.#-/\\\\()]","").toUpperCase());
                                grrbei.setExternalIds(rrmAos);

                                try {
                                    scheduleRecording.setRecorderSettings(getRemoteRecorderSettingsForLocation(rrmStub,grrbei));
                                } catch(NullPointerException e) {
                                    System.out.println("Error: Remote recorder not found or disconnected ("+current+")");
                                }
                                break;
                        //external id
                        case 5: csvStyleOutput(current);
                                externalId = current;
                                break;
                        default: break;
                    }
                    columnNumber++;
                }

                System.out.print("...created\n");
                // if the external id is already in the hash map panoptoScheduleRecordings add to its schedule,
                if (panoptoScheduleRecordings.containsKey(externalId)) {
                    panoptoScheduleRecordings.get(externalId).add(scheduleRecording);
                } else { // otherwise create a new external id in panoptoScheduleRecordings and add the recording to its schedule
                    List<ScheduleRecording> activityList = new ArrayList<ScheduleRecording>();
                    activityList.add(scheduleRecording);
                    panoptoScheduleRecordings.put(externalId, activityList);
                }
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e.getMessage());
            }
        }
        return panoptoScheduleRecordings;
    }
}
