package TT2UTIL::DB;

use strict;
use DBI;


my $dbh =  DBI->connect( 'dbi:Oracle:medinfo','DICOM_USER','EPy8jC5l');
my $sth = $dbh->prepare('select * from  lpu.v_patient v') || die $dbh->errstr;

$sth->execute() || die $dbh->errstr;

while(my $row = $sth->fetchrow_hashref){
	print $row->{'ID'} . ';' . $row->{'SUR_NAME'} . ';' .$row->{'FIRST_NAME'} . ';' .$row->{'PATR_NAME'} . 
	';' .$row->{'CODE'} . ';' .$row->{'BIRTHDAY'} . ';' . $row->{'SEX'} . ';' . ';' . $row->{'UPD_DATE'} . "\n";
}				      





