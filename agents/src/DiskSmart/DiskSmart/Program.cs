using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Management;
using System.Collections;

namespace DiskSmart
{
    class Program
    {
        //S.M.A.R.T.  Temperature attritube

        const byte TEMPERATURE_ATTRIBUTE = 194;


        static void Main(string[] args)
        {
           try
            {
                ManagementObjectSearcher searcher = new ManagementObjectSearcher("root\\WMI", "SELECT * FROM MSStorageDriver_ATAPISmartData");
                //loop through all the hard disks
                int idx = 0;
                foreach (ManagementObject queryObj in searcher.Get())
                {
                    byte[] arrVendorSpecific = (byte[])queryObj.GetPropertyValue("VendorSpecific");
                    //Find the temperature attribute
                    int tempIndex = Array.IndexOf(arrVendorSpecific, TEMPERATURE_ATTRIBUTE);
                    String s = arrVendorSpecific[tempIndex + 5].ToString();
                    Console.WriteLine("hddTemp"+idx+++"=" + s);
                }
            }
            catch (ManagementException err)
            {
                return;
            }
        }
    }
}
