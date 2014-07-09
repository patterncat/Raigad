/**
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.elasticcar.configuration;

import com.google.inject.ImplementedBy;

import java.util.List;


@ImplementedBy(ElasticCarConfiguration.class)
public interface IConfiguration
{
	
    public void initialize();

    /**
     * @return Path to the home dir of Elasticsearch
     */
    public String getElasticsearchHome();

    public String getYamlLocation();

    public String getBackupLocation();

    /**
     * @return Path to Elasticsearch startup script
     */
    public String getElasticsearchStartupScript();

    /**
     * @return Path to Elasticsearch stop sript
     */
    public String getElasticsearchStopScript();
   
    public int getElasticsearchListenerPort();
        
    public int getTransportTcpPort();
    
    public int getHttpPort();

    public int getNumOfShards();
    
    public int getNumOfReplicas();

    public int getTotalShardsPerNode();

    public String getRefreshInterval();
    
    public boolean isMasterQuorumEnabled();
    
    public int getMinimumMasterNodes();
    
    public String getPingTimeout();
    
    public boolean isPingMulticastEnabled();
    
    public String getFdPingInterval();
    
    public String getFdPingTimeout();   

    /**
     * @return Location of the local data dir
     */
	public String getDataFileLocation();

    /**
     * @return Location of the local log dir
     */
	public String getLogFileLocation();

	public boolean doesElasticsearchStartManually();

    /**
     * @return Cluster name
     */
    public String getAppName();

    /**
     * @return RAC (or zone for AWS)
     */
    public String getRac();

    /**
     * @return List of all RAC used for the cluster
     */
    public List<String> getRacs();

    /**
     * @return Local hostmame
     */
    public String getHostname();

    /**
     * @return Get instance name (for AWS)
     */
    public String getInstanceName();

    /**
     * @return Get instance id (for AWS)
     */
    public String getInstanceId();


    /**
     * @return Get the Data Center name (or region for AWS)
     */
    public String getDC();

    /**
     * @param dc
     *            Set the current data center
     */
    public void setDC(String dc);

 
    /**
     * Amazon specific setting to query ASG Membership
     */
    public String getASGName();
    
    /**
     * Get the security group associated with nodes in this cluster
     */
    public String getACLGroupName();

   
    /**
     * @return Get host Public IP
     */
    public String getHostIP();

    /**
     * @return Get host Local IP
     */
    public String getHostLocalIP();
   
    /**
     * @return Bootstrap cluster name (depends on another cass cluster)
     */
    public String getBootClusterName();
    
    /** 
     * @return Get the name of seed provider
     */
    public String getSeedProviderName();

    /**
     * @return Elasticsearch Process Name
     */
    public String getElasticsearchProcessName();

    /**
     * @return Elasticsearch Discovery Type
     */
    public String getElasticsearchDiscoveryType();


	public boolean isMultiDC();

    /**
     * @return Elasticsearch Index Refresh Interval
     */
	public String getIndexRefreshInterval();

    public String getClusterRoutingAttributes();
    
    public boolean isAsgBasedDedicatedDeployment();

    public boolean isCustomShardAllocationPolicyEnabled();

    public String getClusterShardAllocationAttribute();

    /**
     * Providing a way to add New Config Params without any code change
     */
    public String getExtraConfigParams();

    public String getEsKeyName(String escarKey);

    public boolean isDebugEnabled();

    public boolean isShardPerNodeEnabled();

    public boolean isIndexAutoCreationEnabled();

    public String getIndexMetadata();

    public int getAutoCreateIndexTimeout();

    public int getAutoCreateIndexInitialStartDelaySeconds();

    public int getAutoCreateIndexPeriodicScheduledDelaySeconds();

    /*
        Backup related Config properties
    */
    public String getCommaSeparatedIndicesToBackup();

    public String ignoreUnavailableIndicesDuringBackup();

    public boolean includeGlobalStateDuringBackup();

    public boolean waitForCompletionOfBackup();

    public boolean includeIndexNameInSnapshot();

    /**
     * @return Backup hour for snapshot backups (0 - 23)
     */
    public int getBackupHour();


}