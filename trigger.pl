#!/usr/bin/env perl
use strict;
use Net::Stomp;

sub create_model {
    my ($item, $lot, $operator, $when, $org, $wafers) = @_;
    my $xml = qq(<lot name="$lot" item="$item" organization="$org">\n);
    for my $w(@{eval($wafers)}) {
        my %wafer = %$w;
        $xml .= qq(  <wafer number="$wafer{number}" passdies="$wafer{passdies}" />\n);
    }
    $xml .= "</lot>\n";
}

sub sendTrigger {
    my ($hostname, $port, $body) = @_;
    print "Connecting to $hostname $port\n";
    my $stomp = Net::Stomp->new({ hostname => $hostname, port => $port }) or die "Could not connect ". $!;
    $stomp->connect or die "Could not connect ". $!;
    $stomp->send({ destination => "/queue/finished_postprocessing",
               body => $body }) or die "Could not send message ". $!;

}

if ($#ARGV != 5) {
    die "The script should be called with arguments item, lot, operation, when, org, wafers.";
}

my $body = create_model(@ARGV);
sendTrigger("localhost", 61613, $body);

