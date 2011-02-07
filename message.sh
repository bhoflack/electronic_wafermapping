#!/bin/sh

tempfile=`mktemp -t postprocess_job_XXXXXXXX`

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" >> $tempfile
echo "  <event>" >> $tempfile
echo "    <type>entered_postprocessing</type>" >> $tempfile
echo "    <lotname>A84001</lotname>" >> $tempfile
echo "    <item>201234500</item>" >> $tempfile
echo "    <when>20110207-16:02</when>" >> $tempfile
echo "    <operator>brh</operator>" >> $tempfile
echo "    <probing-site>IEPER</probing-site>" >> $tempfile
echo "    <wafers>" >> $tempfile
echo "      <wafer>1</wafer>" >> $tempfile
echo "      <wafer>2</wafer>" >> $tempfile
echo "    </wafers>" >> $tempfile
echo "  </event>" >> $tempfile

mv $tempfile /tmp/postprocessing
